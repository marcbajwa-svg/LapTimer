package com.marcbajwa.laptimernative.data

import com.marcbajwa.laptimernative.model.LiveSessionSnapshot
import com.marcbajwa.laptimernative.model.TrackPreset

object TrackRepository {
    val nearbySuggestion = TrackPreset(
        id = "pannoniaring",
        name = "Pannonia-Ring",
        country = "Hungary",
        markerLabel = "Main straight start / finish",
        minimumLapSeconds = 110,
        distanceLabel = "1.1 km away",
    )

    val presets = listOf(
        nearbySuggestion,
        TrackPreset(
            id = "anneau-du-rhin",
            name = "Anneau du Rhin",
            country = "France",
            markerLabel = "Main straight start / finish",
            minimumLapSeconds = 75,
        ),
        TrackPreset(
            id = "hockenheimring",
            name = "Hockenheimring",
            country = "Germany",
            markerLabel = "Start / finish on the main straight",
            minimumLapSeconds = 95,
        ),
    )

    val liveSnapshot = LiveSessionSnapshot(
        currentLap = "01:47.38",
        currentDelta = "-00:00.42",
        lastLap = "01:48.10",
        bestLap = "01:47.80",
        totalLaps = 5,
        gpsStatus = "Stable GPS lock",
    )
}
