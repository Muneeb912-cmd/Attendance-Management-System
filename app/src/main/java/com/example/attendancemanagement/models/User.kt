package com.example.attendancemanagement.models

import java.io.Serializable

data class User(
    var userId:String="",
    var userName:String="",
    var userEmail:String="",
    var userPhoto:String=""
):Serializable
