package com.rndio.gpstesx.data.local.dao

import androidx.room.*
import com.rndio.gpstesx.data.local.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Delete
    suspend fun deleteUser(user: User)
}
