package com.example.attendancemanagement.models

import com.example.attendancemanagement.room_db.MarkAttendance
import android.content.Context
import com.example.attendancemanagement.room_db.Attendance
import com.example.attendancemanagement.room_db.AttendanceDB
import com.example.attendancemanagement.room_db.AttendanceDao

import com.example.attendancemanagement.room_db.Session
import com.example.attendancemanagement.room_db.SessionClass
import com.example.attendancemanagement.room_db.Student

class AttendanceRepository(context: Context) {
    private var db: AttendanceDB = AttendanceDB.getInstance(context)
    private var attendanceDao: AttendanceDao = db.attendanceDao()

    suspend fun createSession(sessionTitle: String) {
        val newSession = Session(sessionTitle = sessionTitle)
        attendanceDao.createSession(newSession)
    }

    suspend fun createClass(sessionTitle: String, classTitle: String) {
        val session = attendanceDao.getSessionIdByTitle(sessionTitle)
        val newClass = SessionClass(sessionId = session, classTitle = classTitle)
        attendanceDao.upsertSessionClass(newClass)
    }

    suspend fun removeClass(sessionClass: SessionClass) {
        attendanceDao.deleteClass(sessionClass = sessionClass)
    }

    suspend fun getAllClassesBySession(sessionTitle: String): List<SessionClass> {
        val session = attendanceDao.getSessionIdByTitle(sessionTitle)
        return attendanceDao.getAllClassesBySession(session)
    }

    suspend fun addStudentToClass(user: User, sessionTitle: String, classId: Int) {
        val session = attendanceDao.getSessionIdByTitle(sessionTitle)
        val student = Student(
            sessionId = session,
            studentId = user.userId,
            classId = classId,
            imgUrl = user.userPhoto,
            studentName = user.userName
        )
        attendanceDao.upsertStudent(student)
    }

    suspend fun removeStudentToClass(user: User, sessionTitle: String, classId: Int) {
        val student: Student = Student(
            sessionId = attendanceDao.getSessionIdByTitle(sessionTitle),
            studentId = user.userId,
            classId = classId,
            imgUrl = user.userPhoto,
            studentName = user.userName
        )
        attendanceDao.deleteStudent(student)
    }

    suspend fun getAllSessions(): List<Session> {
        return attendanceDao.getAllSession()
    }

    suspend fun createAttendance(attendance: Attendance){
        attendanceDao.createAttendance(attendance)
    }

    suspend fun getAllAttendance(): List<Attendance>{
        return attendanceDao.getAllAttendance()
    }


    suspend fun getStudentsByClass(classId: Int):MutableList<Student>{
        return attendanceDao.getAllStudentsByClass(classId)
    }

    suspend fun markAttendance(attendance: MarkAttendance){
        attendanceDao.markAttendance(attendance)
    }

    suspend fun unMarkAttendance(attendance: MarkAttendance){
        attendanceDao.markAttendance(attendance)
    }

    suspend fun getStudentAttendance(studentId:String,classId: Int):MarkAttendance{
        return attendanceDao.getStudentAttendance(studentId,classId)
    }

    suspend fun getStudentData(studentId: String):StudentData{
        return attendanceDao.getStudentData(studentId)
    }

}