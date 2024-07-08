package com.example.attendancemanagement.models

data class SessionClassStudent(
    var sessionTitle:String,
    var sessionClass:String,
    var studentsLst:List<User>
)
