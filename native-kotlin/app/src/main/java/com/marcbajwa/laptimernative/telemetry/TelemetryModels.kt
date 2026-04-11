package com.marcbajwa.laptimernative.telemetry

data class TelemetrySample(
    val timestampMillis: Long,
    val latitude: Double,
    val longitude: Double,
    val speedKmh: Float?,
    val gpsAccuracyMeters: Float?,
    val leanDegrees: Float?,
    val lapIndex: Int,
)
