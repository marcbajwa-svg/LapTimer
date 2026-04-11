package com.marcbajwa.laptimernative.data

import android.content.Context
import com.marcbajwa.laptimernative.model.TrackPreset

class LocalLapTimerStore(context: Context) {
    private val preferences = context.getSharedPreferences("lap_timer_store", Context.MODE_PRIVATE)

    fun loadManualTrack(): TrackPreset? {
        if (!preferences.getBoolean(KEY_MANUAL_TRACK_EXISTS, false)) {
            return null
        }

        return TrackPreset(
            id = preferences.getString(KEY_MANUAL_TRACK_ID, null) ?: return null,
            name = preferences.getString(KEY_MANUAL_TRACK_NAME, null) ?: return null,
            country = preferences.getString(KEY_MANUAL_TRACK_COUNTRY, null) ?: return null,
            markerLabel = preferences.getString(KEY_MANUAL_TRACK_MARKER, null) ?: return null,
            minimumLapSeconds = preferences.getInt(KEY_MANUAL_TRACK_MIN_LAP_SECONDS, 10),
            latitude = Double.fromBits(preferences.getLong(KEY_MANUAL_TRACK_LATITUDE, 0L)),
            longitude = Double.fromBits(preferences.getLong(KEY_MANUAL_TRACK_LONGITUDE, 0L)),
            suggestionRadiusMeters = preferences.getInt(KEY_MANUAL_TRACK_RADIUS, 100),
            startHeadingDegrees = preferences.readNullableDouble(KEY_MANUAL_TRACK_HEADING),
            startLineHalfWidthMeters = Double.fromBits(preferences.getLong(KEY_MANUAL_TRACK_HALF_WIDTH, 35.0.toBits())),
            distanceLabel = preferences.getString(KEY_MANUAL_TRACK_DISTANCE, null),
        )
    }

    fun saveManualTrack(track: TrackPreset) {
        preferences.edit()
            .putBoolean(KEY_MANUAL_TRACK_EXISTS, true)
            .putString(KEY_MANUAL_TRACK_ID, track.id)
            .putString(KEY_MANUAL_TRACK_NAME, track.name)
            .putString(KEY_MANUAL_TRACK_COUNTRY, track.country)
            .putString(KEY_MANUAL_TRACK_MARKER, track.markerLabel)
            .putInt(KEY_MANUAL_TRACK_MIN_LAP_SECONDS, track.minimumLapSeconds)
            .putLong(KEY_MANUAL_TRACK_LATITUDE, track.latitude.toBits())
            .putLong(KEY_MANUAL_TRACK_LONGITUDE, track.longitude.toBits())
            .putInt(KEY_MANUAL_TRACK_RADIUS, track.suggestionRadiusMeters)
            .writeNullableDouble(KEY_MANUAL_TRACK_HEADING, track.startHeadingDegrees)
            .putLong(KEY_MANUAL_TRACK_HALF_WIDTH, track.startLineHalfWidthMeters.toBits())
            .putString(KEY_MANUAL_TRACK_DISTANCE, track.distanceLabel)
            .apply()
    }

    fun loadBestLapMillis(trackId: String): Long? {
        val value = preferences.getLong(bestLapKey(trackId), NO_TIME)
        return value.takeIf { it != NO_TIME }
    }

    fun saveBestLapMillis(trackId: String, bestLapMillis: Long) {
        val storedBest = loadBestLapMillis(trackId)
        if (storedBest == null || bestLapMillis < storedBest) {
            preferences.edit().putLong(bestLapKey(trackId), bestLapMillis).apply()
        }
    }

    fun saveLastSession(
        track: TrackPreset,
        totalLaps: Int,
        bestLapMillis: Long?,
        lastLapMillis: Long?,
        maxLeftLeanDegrees: Float,
        maxRightLeanDegrees: Float,
    ) {
        preferences.edit()
            .putString(KEY_LAST_SESSION_TRACK_NAME, track.name)
            .putInt(KEY_LAST_SESSION_TOTAL_LAPS, totalLaps)
            .putLong(KEY_LAST_SESSION_BEST_LAP, bestLapMillis ?: NO_TIME)
            .putLong(KEY_LAST_SESSION_LAST_LAP, lastLapMillis ?: NO_TIME)
            .putFloat(KEY_LAST_SESSION_MAX_LEFT_LEAN, maxLeftLeanDegrees)
            .putFloat(KEY_LAST_SESSION_MAX_RIGHT_LEAN, maxRightLeanDegrees)
            .putLong(KEY_LAST_SESSION_SAVED_AT, System.currentTimeMillis())
            .apply()
    }

    private fun bestLapKey(trackId: String): String {
        return "best_lap_$trackId"
    }

    private fun android.content.SharedPreferences.readNullableDouble(key: String): Double? {
        if (!contains(key)) {
            return null
        }
        return Double.fromBits(getLong(key, 0L))
    }

    private fun android.content.SharedPreferences.Editor.writeNullableDouble(
        key: String,
        value: Double?,
    ): android.content.SharedPreferences.Editor {
        return if (value == null) {
            remove(key)
        } else {
            putLong(key, value.toBits())
        }
    }

    companion object {
        private const val NO_TIME = -1L
        private const val KEY_MANUAL_TRACK_EXISTS = "manual_track_exists"
        private const val KEY_MANUAL_TRACK_ID = "manual_track_id"
        private const val KEY_MANUAL_TRACK_NAME = "manual_track_name"
        private const val KEY_MANUAL_TRACK_COUNTRY = "manual_track_country"
        private const val KEY_MANUAL_TRACK_MARKER = "manual_track_marker"
        private const val KEY_MANUAL_TRACK_MIN_LAP_SECONDS = "manual_track_min_lap_seconds"
        private const val KEY_MANUAL_TRACK_LATITUDE = "manual_track_latitude"
        private const val KEY_MANUAL_TRACK_LONGITUDE = "manual_track_longitude"
        private const val KEY_MANUAL_TRACK_RADIUS = "manual_track_radius"
        private const val KEY_MANUAL_TRACK_HEADING = "manual_track_heading"
        private const val KEY_MANUAL_TRACK_HALF_WIDTH = "manual_track_half_width"
        private const val KEY_MANUAL_TRACK_DISTANCE = "manual_track_distance"
        private const val KEY_LAST_SESSION_TRACK_NAME = "last_session_track_name"
        private const val KEY_LAST_SESSION_TOTAL_LAPS = "last_session_total_laps"
        private const val KEY_LAST_SESSION_BEST_LAP = "last_session_best_lap"
        private const val KEY_LAST_SESSION_LAST_LAP = "last_session_last_lap"
        private const val KEY_LAST_SESSION_MAX_LEFT_LEAN = "last_session_max_left_lean"
        private const val KEY_LAST_SESSION_MAX_RIGHT_LEAN = "last_session_max_right_lean"
        private const val KEY_LAST_SESSION_SAVED_AT = "last_session_saved_at"
    }
}
