package com.marcbajwa.laptimernative.telemetry

import android.content.Context
import com.marcbajwa.laptimernative.model.TrackPreset
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TelemetryExporter(private val context: Context) {
    fun exportCsv(track: TrackPreset, samples: List<TelemetrySample>): File? {
        if (samples.isEmpty()) {
            return null
        }

        val directory = File(context.filesDir, "telemetry")
        directory.mkdirs()

        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
        val safeTrackName = track.name.lowercase(Locale.US)
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
            .ifBlank { "session" }
        val file = File(directory, "$timestamp-$safeTrackName.csv")

        file.bufferedWriter().use { writer ->
            writer.appendLine("timestamp_ms,track,lap_index,latitude,longitude,speed_kmh,gps_accuracy_m,lean_deg")
            samples.forEach { sample ->
                writer.appendLine(
                    listOf(
                        sample.timestampMillis.toString(),
                        track.name.csvEscape(),
                        sample.lapIndex.toString(),
                        sample.latitude.formatNumber(),
                        sample.longitude.formatNumber(),
                        sample.speedKmh.formatNumber(),
                        sample.gpsAccuracyMeters.formatNumber(),
                        sample.leanDegrees.formatNumber(),
                    ).joinToString(","),
                )
            }
        }

        return file
    }
}

private fun String.csvEscape(): String {
    return "\"" + replace("\"", "\"\"") + "\""
}

private fun Double.formatNumber(): String {
    return String.format(Locale.US, "%.7f", this)
}

private fun Float?.formatNumber(): String {
    return this?.let { String.format(Locale.US, "%.2f", it) } ?: ""
}
