package com.rndio.gpstesx.repository

import com.rndio.gpstesx.data.local.dao.UserDao
import com.rndio.gpstesx.data.local.entities.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}