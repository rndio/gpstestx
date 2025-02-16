package com.rndio.gpstesx.ui.main

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rndio.gpstesx.R
import com.rndio.gpstesx.data.local.entities.User
import com.rndio.gpstesx.ui.adapter.UserAdapter
import com.rndio.gpstesx.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi UI
        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        btnAdd = findViewById(R.id.btn_add)
        recyclerView = findViewById(R.id.recycler_view)

        // Setup RecyclerView
        userAdapter = UserAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        // Observe LiveData dari ViewModel
        userViewModel.allUsers.observe(this) { users ->
            userAdapter.updateData(users)
        }

        // Handle klik tombol tambah
        btnAdd.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty()) {
                userViewModel.insertUser(name, email)
                edtName.text.clear()
                edtEmail.text.clear()
            }
        }
    }
}