package com.example.attendancemanagement.room_db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(tableName = "session_class",
    foreignKeys = [
        ForeignKey(
            entity =Session::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        ),
    ])
data class SessionClass(
    val sessionId:Int,
    val classTitle:String,
    @PrimaryKey(autoGenerate = true)
    val classId:Int=0
)
