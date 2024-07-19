package com.example.attendancemanagement

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity


import com.google.firebase.FirebaseApp


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)


//        // Example operation
//        lifecycleScope.launch {
//            val attendance = Attendance(attendanceId = 1, attendanceTitle = "Attendance 1",dateCreated = System.currentTimeMillis().toString())
//            attendanceDao.upsertAttendance(attendance)
//            val allAttendance = attendanceDao.getAllAttendance()
//            Log.d("MainActivity", "Attendance list: $allAttendance")
//        }

    }
}