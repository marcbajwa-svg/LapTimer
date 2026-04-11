package com.marcbajwa.laptimernative.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.atan2

class LeanAngleTracker(
    context: Context,
    private val onRollDegrees: (Float) -> Unit,
) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var smoothedRollDegrees: Float? = null

    fun start() {
        accelerometer?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val rollDegrees = Math.toDegrees(atan2(x.toDouble(), y.toDouble())).toFloat()
        val smoothed = smoothedRollDegrees?.let { previous ->
            previous + (normalizeDegrees(rollDegrees - previous) * 0.15f)
        } ?: rollDegrees

        smoothedRollDegrees = normalizeDegrees(smoothed)
        onRollDegrees(requireNotNull(smoothedRollDegrees))
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}

fun normalizeDegrees(degrees: Float): Float {
    var normalized = degrees
    while (normalized > 180f) normalized -= 360f
    while (normalized < -180f) normalized += 360f
    return normalized
}
