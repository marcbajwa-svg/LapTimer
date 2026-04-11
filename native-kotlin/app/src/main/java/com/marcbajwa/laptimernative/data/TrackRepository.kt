package com.marcbajwa.laptimernative.data

import com.marcbajwa.laptimernative.model.CurrentPosition
import com.marcbajwa.laptimernative.model.LiveSessionSnapshot
import com.marcbajwa.laptimernative.model.TrackPreset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

object TrackRepository {
    val nearbySuggestion = TrackPreset(
        id = "pannoniaring",
        name = "Pannonia-Ring",
        country = "Hungary",
        markerLabel = "Main straight start / finish",
        minimumLapSeconds = 110,
        latitude = 47.3043,
        longitude = 17.0467,
        suggestionRadiusMeters = 2_500,
        startHeadingDegrees = 80.0,
    )

    val presets = listOf(
        nearbySuggestion,
        TrackPreset(
            id = "anneau-du-rhin",
            name = "Anneau du Rhin",
            country = "France",
            markerLabel = "Main straight start / finish",
            minimumLapSeconds = 75,
            latitude = 47.9461,
            longitude = 7.4296,
            suggestionRadiusMeters = 2_000,
            startHeadingDegrees = 20.0,
        ),
        TrackPreset(
            id = "hockenheimring",
            name = "Hockenheimring",
            country = "Germany",
            markerLabel = "Start / finish on the main straight",
            minimumLapSeconds = 95,
            latitude = 49.3278,
            longitude = 8.5658,
            suggestionRadiusMeters = 2_500,
            startHeadingDegrees = 105.0,
        ),
    )

    val liveSnapshot = LiveSessionSnapshot(
        currentLap = "01:47.38",
        currentDelta = "-00:00.42",
        lastLap = "01:48.10",
        bestLap = "01:47.80",
        totalLaps = 5,
        gpsStatus = "Stable GPS lock",
        speedLabel = "-- km/h",
        leanCurrentLabel = "--°",
        leanLeftLabel = "--°",
        leanRightLabel = "--°",
    )

    fun findNearbyTrack(position: CurrentPosition?): TrackPreset? {
        if (position == null) {
            return null
        }

        return presets
            .map { track -> track to distanceMeters(position.latitude, position.longitude, track.latitude, track.longitude) }
            .filter { (track, distance) -> distance <= track.suggestionRadiusMeters }
            .minByOrNull { (_, distance) -> distance }
            ?.let { (track, distance) -> track.copy(distanceLabel = formatDistance(distance)) }
    }

    fun formatSpeed(position: CurrentPosition?): String {
        val speed = position?.speedKmh ?: return "-- km/h"
        return "${speed.roundToInt()} km/h"
    }

    fun formatAccuracy(position: CurrentPosition?): String {
        val accuracy = position?.accuracyMeters ?: return "GPS wartet"
        return "Genauigkeit ${accuracy.roundToInt()} m"
    }

    fun distanceToTrack(position: CurrentPosition, track: TrackPreset): Double {
        return distanceMeters(position.latitude, position.longitude, track.latitude, track.longitude)
    }

    private fun formatDistance(distanceMeters: Double): String {
        return if (distanceMeters >= 1_000) {
            "${(distanceMeters / 100.0).roundToInt() / 10.0} km away"
        } else {
            "${distanceMeters.roundToInt()} m away"
        }
    }

    private fun distanceMeters(
        fromLatitude: Double,
        fromLongitude: Double,
        toLatitude: Double,
        toLongitude: Double,
    ): Double {
        val earthRadiusMeters = 6_371_000.0
        val latitudeDelta = Math.toRadians(toLatitude - fromLatitude)
        val longitudeDelta = Math.toRadians(toLongitude - fromLongitude)
        val fromLatitudeRad = Math.toRadians(fromLatitude)
        val toLatitudeRad = Math.toRadians(toLatitude)

        val haversine = sin(latitudeDelta / 2) * sin(latitudeDelta / 2) +
            cos(fromLatitudeRad) * cos(toLatitudeRad) *
            sin(longitudeDelta / 2) * sin(longitudeDelta / 2)
        val angularDistance = 2 * atan2(sqrt(haversine), sqrt(1 - haversine))

        return earthRadiusMeters * angularDistance
    }
}
