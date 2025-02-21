package com.rndio.gpstesx.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.rndio.gpstesx.data.local.entities.LocationEntity
import com.rndio.gpstesx.viewmodel.LocationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GpsTrackingService : Service() {

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var lastLocation: Location? = null // 🔹 Untuk mengecek jarak dari lokasi sebelumnya

    override fun onCreate() {
        super.onCreate()
        Log.d("GpsTrackingService", "Service Created")

        // 🔹 Inisialisasi ViewModel
        locationViewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)
            .create(LocationViewModel::class.java)

        // 🔹 Inisialisasi FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 🔹 Inisialisasi callback lokasi
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d("GpsTrackingService", "New Location: ${location.latitude}, ${location.longitude}")
                    saveLocationIfMoved(location)
                }
            }
        }
    }

    private fun createNotification(): Notification {
        val channelId = "gps_tracking_channel"
        val channelName = "GPS Tracking"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("GPS Tracking Active")
            .setContentText("Tracking your location in the background")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("GpsTrackingService", "Service Started")
        startTracking()
        startForeground(1, createNotification())
        return START_STICKY
    }

    private fun startTracking() {
        Log.d("GpsTrackingService", "Tracking Started")

        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 🔹 Ambil lokasi setiap 10 detik
            fastestInterval = 5000 // 🔹 Interval tercepat 5 detik
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun saveLocationIfMoved(location: Location) {
        if (lastLocation == null || lastLocation!!.distanceTo(location) >= 50) { // 🔹 Simpan hanya jika pindah 100 meter
            lastLocation = location

            val locationEntity = LocationEntity(
                timestamp = System.currentTimeMillis(),
                latitude = location.latitude,
                longitude = location.longitude
            )

            CoroutineScope(Dispatchers.IO).launch {
                locationViewModel.saveLocation(locationEntity)
                Log.d("GpsTrackingService", "Location Saved: ${locationEntity.latitude}, ${locationEntity.longitude}")
            }
        } else {
            Log.d("GpsTrackingService", "No Significant Movement")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback) // 🔹 Hentikan update lokasi saat service berhenti
        Log.d("GpsTrackingService", "Service Destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
