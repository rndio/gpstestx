package com.rndio.gpstesx.utils

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

object LocationHelper {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun requestLocationUpdates(context: Context) = callbackFlow<Location> {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                for (location in result.locations) {
                    trySend(location)
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
    }
}