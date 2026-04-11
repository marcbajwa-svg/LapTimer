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
    private val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
        ?: sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val fallbackAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private var smoothedRollDegrees: Float? = null

    fun start() {
        (rotationSensor ?: fallbackAccelerometer)?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val rollDegrees = when (event.sensor.type) {
            Sensor.TYPE_GAME_ROTATION_VECTOR,
            Sensor.TYPE_ROTATION_VECTOR -> event.rotationVectorRollDegrees()
            else -> event.accelerometerRollDegrees()
        }
        val smoothed = smoothedRollDegrees?.let { previous ->
            previous + (normalizeDegrees(rollDegrees - previous) * 0.15f)
        } ?: rollDegrees

        smoothedRollDegrees = normalizeDegrees(smoothed)
        onRollDegrees(requireNotNull(smoothedRollDegrees))
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun SensorEvent.rotationVectorRollDegrees(): Float {
        SensorManager.getRotationMatrixFromVector(rotationMatrix, values)
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
        return Math.toDegrees(orientationAngles[2].toDouble()).toFloat()
    }

    private fun SensorEvent.accelerometerRollDegrees(): Float {
        val x = values[0]
        val y = values[1]
        return Math.toDegrees(atan2(x.toDouble(), y.toDouble())).toFloat()
    }
}

fun normalizeDegrees(degrees: Float): Float {
    var normalized = degrees
    while (normalized > 180f) normalized -= 360f
    while (normalized < -180f) normalized += 360f
    return normalized
}
