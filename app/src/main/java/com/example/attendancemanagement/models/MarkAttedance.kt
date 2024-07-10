package com.example.attendancemanagement.models

data class MarkAttendance (
    var studentId:String,
    var studentName:String,
    var attendanceId:Int,
    var className:String,
    var classId:Int,
    var sessionId:Int,
    var sessionName:String,
)