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
    val distanceLabel: String? = null,
)

data class LiveSessionSnapshot(
    val currentLap: String,
    val currentDelta: String,
    val lastLap: String,
    val bestLap: String,
    val totalLaps: Int,
    val gpsStatus: String,
)
