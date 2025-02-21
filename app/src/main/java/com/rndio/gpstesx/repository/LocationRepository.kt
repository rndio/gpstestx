package com.rndio.gpstesx.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.rndio.gpstesx.data.local.AppDatabase
import com.rndio.gpstesx.data.local.entities.LocationEntity

class LocationRepository(context: Context) {
    private val locationDao = AppDatabase.getDatabase(context).locationDao()

    val allLocations: LiveData<List<LocationEntity>> = locationDao.getAllLocationsLive()

    // 🔹 Pastikan menerima LocationEntity, bukan Location langsung
    suspend fun saveLocation(locationEntity: LocationEntity) {
        locationDao.insertLocation(locationEntity)
    }

    fun getAllLocationsLive(): LiveData<List<LocationEntity>> {
        return locationDao.getAllLocationsLive()
    }

    suspend fun deleteAllLocations() {
        locationDao.deleteAllLocations()
    }
}