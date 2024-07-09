package com.example.attendancemanagement.room_db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "session")
data class Session(
    val sessionTitle: String,
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int = 0
)
