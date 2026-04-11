package com.marcbajwa.laptimernative.timing

import com.marcbajwa.laptimernative.data.TrackRepository
import com.marcbajwa.laptimernative.model.CurrentPosition
import com.marcbajwa.laptimernative.model.LapTimingState
import com.marcbajwa.laptimernative.model.TrackPreset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class LapTimingEngine {
    private var activeTrackId: String? = null
    private var activeLapStartMillis: Long? = null
    private var pausedAtMillis: Long? = null
    private var hasLeftStartZone = false
    private var learnedStartHeadingDegrees: Double? = null
    private var previousStartProjection: StartProjection? = null
    private var state = LapTimingState(status = "Zum Startpunkt fahren")

    fun update(position: CurrentPosition, track: TrackPreset): LapTimingState {
        if (activeTrackId != track.id) {
            reset(track)
        }

        val distanceToStart = TrackRepository.distanceToTrack(position, track)
        val now = position.elapsedRealtimeMillis
        val lapStart = activeLapStartMillis
        val startHeading = track.startHeadingDegrees ?: learnedStartHeadingDegrees
        val currentProjection = startHeading?.let { position.projectFromStart(track, it) }
        val crossedStartLine = currentProjection?.let { projection ->
            previousStartProjection?.let { previous ->
                previous.forwardMeters < 0.0 &&
                    projection.forwardMeters >= 0.0 &&
                    kotlin.math.abs(projection.lateralMeters) <= track.startLineHalfWidthMeters
            }
        } ?: false

        if (pausedAtMillis != null) {
            return state
        }

        if (!position.hasTimingAccuracy()) {
            return state.copy(
                currentLapMillis = lapStart?.let { max(0L, now - it) } ?: state.currentLapMillis,
                status = "GPS zu ungenau",
            ).also { state = it }
        }

        state = when {
            lapStart == null && distanceToStart <= START_TRIGGER_RADIUS_METERS -> {
                activeLapStartMillis = now
                hasLeftStartZone = false
                previousStartProjection = currentProjection
                state.copy(currentLapMillis = 0L, status = "Runde laeuft")
            }
            lapStart == null -> {
                state.copy(status = "Zum Startpunkt fahren")
            }
            else -> {
                val currentLapMillis = max(0L, now - lapStart)
                if (distanceToStart > REARM_RADIUS_METERS) {
                    hasLeftStartZone = true
                    if (learnedStartHeadingDegrees == null && track.startHeadingDegrees == null) {
                        learnedStartHeadingDegrees = bearingFromStart(track, position)
                    }
                }

                if (
                    hasLeftStartZone &&
                    (crossedStartLine || (startHeading == null && distanceToStart <= START_TRIGGER_RADIUS_METERS)) &&
                    position.hasLapFinishSpeed() &&
                    currentLapMillis >= track.minimumLapSeconds * 1_000L
                ) {
                    val bestLapMillis = state.bestLapMillis
                    val newBestLapMillis = bestLapMillis?.let { minOf(it, currentLapMillis) } ?: currentLapMillis
                    activeLapStartMillis = now
                    hasLeftStartZone = false
                    state.copy(
                        currentLapMillis = 0L,
                        currentDeltaMillis = 0L,
                        lastLapMillis = currentLapMillis,
                        bestLapMillis = newBestLapMillis,
                        totalLaps = state.totalLaps + 1,
                        status = "Runde gespeichert",
                    ).also { previousStartProjection = currentProjection }
                } else {
                    val bestLapMillis = state.bestLapMillis
                    state.copy(
                        currentLapMillis = currentLapMillis,
                        currentDeltaMillis = bestLapMillis?.let { currentLapMillis - it },
                        status = when {
                            hasLeftStartZone && !position.hasLapFinishSpeed() -> "Zielzone scharf - Tempo fehlt"
                            hasLeftStartZone && startHeading != null -> "Ziellinie scharf"
                            hasLeftStartZone -> "Ziellinie wird gelernt"
                            else -> "Raus aus der Startzone"
                        },
                    ).also { previousStartProjection = currentProjection }
                }
            }
        }

        return state
    }

    fun markManualLap(): LapTimingState {
        if (pausedAtMillis != null) {
            return state
        }
        val lapStart = activeLapStartMillis ?: return state
        val now = android.os.SystemClock.elapsedRealtime()
        val currentLapMillis = max(0L, now - lapStart)
        val bestLapMillis = state.bestLapMillis
        val newBestLapMillis = bestLapMillis?.let { minOf(it, currentLapMillis) } ?: currentLapMillis
        activeLapStartMillis = now
        hasLeftStartZone = false
        state = state.copy(
            currentLapMillis = 0L,
            currentDeltaMillis = 0L,
            lastLapMillis = currentLapMillis,
            bestLapMillis = newBestLapMillis,
            totalLaps = state.totalLaps + 1,
            status = "Runde manuell gespeichert",
        )
        return state
    }

    fun pause(): LapTimingState {
        if (pausedAtMillis == null) {
            pausedAtMillis = android.os.SystemClock.elapsedRealtime()
            state = state.copy(status = "Pausiert")
        }
        return state
    }

    fun resume(): LapTimingState {
        val pausedAt = pausedAtMillis ?: return state
        val now = android.os.SystemClock.elapsedRealtime()
        activeLapStartMillis = activeLapStartMillis?.plus(now - pausedAt)
        pausedAtMillis = null
        state = state.copy(status = "Runde laeuft")
        return state
    }

    fun end(): LapTimingState {
        pausedAtMillis = android.os.SystemClock.elapsedRealtime()
        state = state.copy(status = "Session beendet")
        return state
    }

    fun reset(track: TrackPreset): LapTimingState {
        activeTrackId = track.id
        activeLapStartMillis = null
        pausedAtMillis = null
        hasLeftStartZone = false
        learnedStartHeadingDegrees = track.startHeadingDegrees
        previousStartProjection = null
        state = LapTimingState(status = "Zum Startpunkt fahren")
        return state
    }

    companion object {
        private const val START_TRIGGER_RADIUS_METERS = 25.0
        private const val REARM_RADIUS_METERS = 60.0
    }
}

private data class StartProjection(
    val forwardMeters: Double,
    val lateralMeters: Double,
)

private fun CurrentPosition.hasTimingAccuracy(): Boolean {
    val accuracy = accuracyMeters ?: return false
    return accuracy <= 35f
}

private fun CurrentPosition.hasLapFinishSpeed(): Boolean {
    val speed = speedKmh ?: return false
    return speed >= 12f
}

private fun CurrentPosition.projectFromStart(track: TrackPreset, headingDegrees: Double): StartProjection {
    val metersPerDegreeLatitude = 111_320.0
    val metersPerDegreeLongitude = metersPerDegreeLatitude * cos(Math.toRadians(track.latitude))
    val northMeters = (latitude - track.latitude) * metersPerDegreeLatitude
    val eastMeters = (longitude - track.longitude) * metersPerDegreeLongitude
    val headingRadians = Math.toRadians(headingDegrees)

    return StartProjection(
        forwardMeters = northMeters * cos(headingRadians) + eastMeters * sin(headingRadians),
        lateralMeters = -northMeters * sin(headingRadians) + eastMeters * cos(headingRadians),
    )
}

private fun bearingFromStart(track: TrackPreset, position: CurrentPosition): Double {
    val startLatitude = Math.toRadians(track.latitude)
    val endLatitude = Math.toRadians(position.latitude)
    val longitudeDelta = Math.toRadians(position.longitude - track.longitude)
    val y = sin(longitudeDelta) * cos(endLatitude)
    val x = cos(startLatitude) * sin(endLatitude) -
        sin(startLatitude) * cos(endLatitude) * cos(longitudeDelta)
    return (Math.toDegrees(atan2(y, x)) + 360.0) % 360.0
}
