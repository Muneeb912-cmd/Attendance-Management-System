package com.example.attendancemanagement.room_db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.attendancemanagement.room_db.Attendance
import com.example.attendancemanagement.room_db.Student

@Entity(
    tableName = "mark_attendance",
    foreignKeys = [
        ForeignKey(
            entity = Attendance::class,
            parentColumns = ["attendanceId"],
            childColumns = ["attendanceId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Student::class,
            parentColumns = ["studentId"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["attendanceId", "studentId"], unique = true)]
)
data class MarkAttendance(
    val attendance: Boolean,
    val attendanceId: Int,
    val studentId: String,
    @PrimaryKey(autoGenerate = true)
    val markAttendanceId: Int = 0
)

