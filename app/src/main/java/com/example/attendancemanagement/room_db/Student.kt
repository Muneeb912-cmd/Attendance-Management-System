package com.example.attendancemanagement.room_db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "student",
    foreignKeys = [
        ForeignKey(
            entity =Attendance::class,
            parentColumns = ["attendanceId"],
            childColumns = ["attendanceId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity =SessionClass::class,
            parentColumns = ["classId"],
            childColumns = ["classId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity =Session::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        ),
    ])
data class Student(
    val attendance:Boolean,
    val attendanceId:Int=0,
    val classId:Int=0,
    val sessionId:Int=0,
    val imgUrl:String,
    @PrimaryKey (autoGenerate = false)
    val studentId:String=""
)