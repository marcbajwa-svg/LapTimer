package com.marcbajwa.laptimernative.model

enum class Screen {
    Home,
    Setup,
    Live,
    Summary,
}

data class TrackPreset(
    val id: String,
    val name: String,
    val country: String,
    val markerLabel: String,
    val minimumLapSeconds: Int,
    val latitude: Double,
    val longitude: Double,
    val suggestionRadiusMeters: Int,
    val startHeadingDegrees: Double? = null,
    val startLineHalfWidthMeters: Double = 35.0,
    val distanceLabel: String? = null,
)

data class CurrentPosition(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float?,
    val speedKmh: Float?,
    val elapsedRealtimeMillis: Long,
)

data class LiveSessionSnapshot(
    val currentLap: String,
    val currentDelta: String,
    val lastLap: String,
    val bestLap: String,
    val totalLaps: Int,
    val gpsStatus: String,
    val speedLabel: String,
)

data class LapTimingState(
    val currentLapMillis: Long = 0L,
    val currentDeltaMillis: Long? = null,
    val lastLapMillis: Long? = null,
    val bestLapMillis: Long? = null,
    val totalLaps: Int = 0,
    val status: String = "Bereit",
)
