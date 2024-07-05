package com.example.attendancemanagement.models

data class StudentAttendance (
    var studentAttendanceId:Int,
    var studentId:Int,
    var attendanceId:Int,
    var attendance:String,
)