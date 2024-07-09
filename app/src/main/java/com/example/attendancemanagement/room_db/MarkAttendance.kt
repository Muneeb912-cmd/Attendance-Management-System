package com.example.attendancemanagement.room_db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(tableName = "mark_attendance",
    foreignKeys = [
        ForeignKey(
            entity =Attendance::class,
            parentColumns = ["attendanceId"],
            childColumns = ["attendanceId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity =Student::class,
            parentColumns = ["studentId"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        ),
    ])
data class MarkAttendance(
    val attendanceId:Int,
    val studentId:Int,
    @PrimaryKey(autoGenerate = true)
    val markAttendanceId:Int=0
)
