package com.rndio.gpstesx.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rndio.gpstesx.data.local.entities.LocationEntity

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM locations ORDER BY timestamp DESC")
    fun getAllLocationsLive(): LiveData<List<LocationEntity>>

    @Query("DELETE FROM locations")
    suspend fun deleteAllLocations()
}