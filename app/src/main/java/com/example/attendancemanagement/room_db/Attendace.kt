package com.example.attendancemanagement.room_db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(tableName = "attendance")
data class Attendance(
    val attendanceTitle:String,
    val dateCreated:String,
    @PrimaryKey(autoGenerate = true)
    val attendanceId:Int=0
)
