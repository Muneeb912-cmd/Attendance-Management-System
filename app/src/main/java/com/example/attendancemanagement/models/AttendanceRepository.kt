package com.example.attendancemanagement.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.attendancemanagement.room_db.AttendanceDB
import com.example.attendancemanagement.room_db.AttendanceDao
import com.example.attendancemanagement.room_db.Session
import com.example.attendancemanagement.room_db.SessionClass

class AttendanceRepository(context: Context) {
    private var db: AttendanceDB = AttendanceDB.getInstance(context)
    private var attendanceDao: AttendanceDao = db.attendanceDao()

    suspend fun createAttendance(){

    }

    suspend fun createSessionClass(){

    }

    suspend fun createSession(sessionTitle:String){
        val newSession = Session(sessionTitle = sessionTitle)
        attendanceDao.createSession(newSession)
    }

    suspend fun createClass(sessionTitle:String,classTitle:String){
        val session=attendanceDao.getSessionIdByTitle(sessionTitle)
        val newClass=SessionClass(sessionId = session, classTitle = classTitle)
        attendanceDao.upsertSessionClass(newClass)
    }

    suspend fun removeClass(sessionClass: SessionClass){
        attendanceDao.deleteClass(sessionClass = sessionClass)
    }

    suspend fun getAllClassesBySession(sessionTitle:String):List<SessionClass>{
        val session=attendanceDao.getSessionIdByTitle(sessionTitle)
        return attendanceDao.getAllClassesBySession(session)
    }
}