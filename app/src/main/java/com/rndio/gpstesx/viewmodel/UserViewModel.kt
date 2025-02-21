//package com.rndio.gpstesx.viewmodel
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.rndio.gpstesx.data.local.AppDatabase
//import com.rndio.gpstesx.data.local.entities.User
//import com.rndio.gpstesx.repository.UserRepository
//import kotlinx.coroutines.launch
//
//class UserViewModel(application: Application) : AndroidViewModel(application) {
//    private val db = AppDatabase.getDatabase(application)
//    private val repository: UserRepository = UserRepository(db.userDao())
//
//    private val _allUsers = MutableLiveData<List<User>>()  // LiveData untuk update otomatis
//    val allUsers: LiveData<List<User>> get() = _allUsers
//
//    init {
//        fetchUsers() // Ambil data saat ViewModel dibuat
//    }
//
//    fun insertUser(name: String, email: String) {
//        viewModelScope.launch {
//            repository.insertUser(User(name = name, email = email))
//            fetchUsers()  // Refresh data setelah insert
//        }
//    }
//
//    private fun fetchUsers() {
//        viewModelScope.launch {
//            _allUsers.postValue(repository.getAllUsers()) // Update LiveData
//        }
//    }
//}