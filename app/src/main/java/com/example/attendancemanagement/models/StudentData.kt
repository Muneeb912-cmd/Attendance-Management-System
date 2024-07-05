package com.example.attendancemanagement.models

data class StudentData(
    var name:String,
    var email:String,
    var age:Int,
    var classId:String,
    var sessionId:Int,
    var attendanceId:Int,
    var sdId:Int,
    var sdAttendance: Boolean,
    var className:String,
    var sessionName:String
)
