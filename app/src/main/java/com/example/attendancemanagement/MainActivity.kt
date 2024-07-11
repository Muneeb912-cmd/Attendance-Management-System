package com.example.attendancemanagement

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.attendancemanagement.room_db.Attendance
import com.example.attendancemanagement.room_db.AttendanceDB
import com.example.attendancemanagement.room_db.AttendanceDao


import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch


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