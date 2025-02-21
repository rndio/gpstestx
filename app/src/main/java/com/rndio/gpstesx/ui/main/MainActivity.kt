package com.rndio.gpstesx.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rndio.gpstesx.R
import com.rndio.gpstesx.service.GpsTrackingService
import com.rndio.gpstesx.viewmodel.LocationViewModel
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var txtLocationList: TextView

    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupButtons();

        txtLocationList = findViewById(R.id.txt_location_list)

        // Observe LiveData agar daftar lokasi otomatis diperbarui
        locationViewModel.allLocations.observe(this, Observer { locations ->
            val locationText = locations.joinToString("\n\n") {
                "${it.latitude}|${it.longitude}|${it.timestamp}"
            }
            txtLocationList.text = locationText
        })
    }

    private fun setupButtons() {
        val btnStartTracking = findViewById<Button>(R.id.btn_start)
        val btnStopTracking = findViewById<Button>(R.id.btn_stop)
        val btnExportJson = findViewById<Button>(R.id.btn_export_json)
        val btnResetTracking = findViewById<Button>(R.id.btn_reset_tracking)

        btnStartTracking.setOnClickListener {
            Log.d("MainActivity", "Start Tracking Clicked")
            startGpsService()
        }

        btnStopTracking.setOnClickListener {
            Log.d("MainActivity", "Stop Tracking Clicked")
            stopGpsService()
        }

        btnExportJson.setOnClickListener {
            Log.d("MainActivity", "Export JSON Clicked")
            exportToJson()
        }

        btnResetTracking.setOnClickListener {
            Log.d("MainActivity", "Reset Tracking Clicked")
            locationViewModel.deleteAllLocations()
        }
    }

    private fun startGpsService() {
        val serviceIntent = Intent(this, GpsTrackingService::class.java)
        startService(serviceIntent)
        Log.d("MainActivity", "GPS Service Started")
    }

    private fun stopGpsService() {
        val serviceIntent = Intent(this, GpsTrackingService::class.java)
        stopService(serviceIntent)
        Log.d("MainActivity", "GPS Service Stopped")
    }

    private fun exportToJson() {
        val locations = locationViewModel.allLocations.value
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(locations)

        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "locations.json")
        try {
            FileWriter(file).use { writer ->
                writer.write(json)
            }
            val uri = FileProvider.getUriForFile(this, "com.rndio.gpstesx.fileprovider", file)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "application/json"
            }
            startActivity(Intent.createChooser(shareIntent, "Export to JSON"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}


