package com.example.attendancemanagement.room_db

sealed interface AttendanceEvents {
    data class CreateSessionClass(
        val sessionClass:SessionClass
    ):AttendanceEvents
}