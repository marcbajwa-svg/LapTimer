package com.marcbajwa.laptimernative.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.marcbajwa.laptimernative.model.CurrentPosition

class NativeLocationTracker(
    context: Context,
    private val onPosition: (CurrentPosition) -> Unit,
) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val listener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            onPosition(location.toCurrentPosition())
        }
    }

    @SuppressLint("MissingPermission")
    fun start() {
        preferredProviders().firstOrNull { provider ->
            try {
                locationManager.getLastKnownLocation(provider)?.let { onPosition(it.toCurrentPosition()) }
                locationManager.requestLocationUpdates(provider, 1_000L, 1f, listener)
                true
            } catch (_: SecurityException) {
                false
            } catch (_: IllegalArgumentException) {
                false
            }
        }
    }

    fun stop() {
        locationManager.removeUpdates(listener)
    }

    private fun preferredProviders(): List<String> {
        return listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
            .filter { provider -> locationManager.isProviderEnabled(provider) }
    }
}

private fun Location.toCurrentPosition(): CurrentPosition {
    return CurrentPosition(
        latitude = latitude,
        longitude = longitude,
        accuracyMeters = if (hasAccuracy()) accuracy else null,
        speedKmh = if (hasSpeed()) speed * 3.6f else null,
    )
}
