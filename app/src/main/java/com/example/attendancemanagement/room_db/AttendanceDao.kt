package com.example.attendancemanagement.room_db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface AttendanceDao {
    @Upsert
    suspend fun upsertAttendance(attendance: Attendance)

    @Delete
    suspend fun deleteAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance")
    suspend fun getAllAttendance(): List<Attendance>

    @Upsert
    suspend fun markAttendance(attendance: Attendance)

    @Query("SELECT * FROM mark_attendance")
    suspend fun getAllMarkedAttendance(): List<MarkAttendance>

    @Query("SELECT * FROM mark_attendance WHERE  studentId = :studentId")
    suspend fun getAttendanceByStudentId(studentId: Int): List<MarkAttendance>

    @Upsert
    suspend fun upsertSessionClass(sessionClass: SessionClass)

    @Delete
    suspend fun deleteSessionClass(session: SessionClass)

    @Query("SELECT * FROM session_class WHERE sessionID = :sessionID")
    suspend fun getAllClassesBySession(sessionID: Int):List<SessionClass>

    @Query("SELECT * FROM session")
    suspend fun getAllSession(): List<Session>

    @Upsert
    suspend fun upsertStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM student")
    suspend fun getAllStudents(): List<Student>

    @Query("SELECT * FROM student WHERE studentId = :studentId")
    suspend fun getStudentById(studentId: Int): Student

    @Upsert
    suspend fun createSession(session: Session)

    @Delete
    suspend fun deleteClass(sessionClass: SessionClass)

    @Query("SELECT sessionId FROM session WHERE sessionTitle = :sessionTitle")
    suspend fun getSessionIdByTitle(sessionTitle: String): Int
}
