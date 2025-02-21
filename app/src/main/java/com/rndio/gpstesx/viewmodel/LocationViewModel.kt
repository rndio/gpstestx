package com.rndio.gpstesx.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rndio.gpstesx.data.local.AppDatabase
import com.rndio.gpstesx.data.local.entities.LocationEntity
import com.rndio.gpstesx.repository.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocationRepository

    val allLocations: LiveData<List<LocationEntity>>

    init {
        AppDatabase.getDatabase(application).locationDao()
        repository = LocationRepository(application)
        allLocations = repository.getAllLocationsLive()
    }

//    fun getAllLocations(): LiveData<List<LocationEntity>> {
//        return allLocations
//    }

    // 🔹 Perbaiki fungsi ini agar menerima Location dari Android API, lalu dikonversi ke LocationEntity
    fun saveLocation(locationEntity: LocationEntity) {
        viewModelScope.launch {
            repository.saveLocation(locationEntity) // 🔹 Pastikan repository menerima LocationEntity
        }
    }

    fun deleteAllLocations() {
        viewModelScope.launch {
            repository.deleteAllLocations()
        }
    }
}