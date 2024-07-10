package com.example.attendancemanagement.room_db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(tableName = "attendance",
    foreignKeys = [
        ForeignKey(
            entity =Session::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity =SessionClass::class,
            parentColumns = ["classId"],
            childColumns = ["classId"],
            onDelete = ForeignKey.CASCADE
        ),
    ])
data class Attendance(
    val attendanceTitle:String,
    val dateCreated:String,
    val sessionId:Int,
    val classId:Int,
    @PrimaryKey(autoGenerate = true)
    val attendanceId:Int=0
)
