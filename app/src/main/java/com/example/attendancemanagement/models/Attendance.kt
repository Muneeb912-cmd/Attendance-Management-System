package com.example.attendancemanagement.models

data class Attendance(
    var attendanceTitle:String,
    var attendanceID:Int,
    var dateCreated:String,
    var studentAttendanceId:Int,
)
