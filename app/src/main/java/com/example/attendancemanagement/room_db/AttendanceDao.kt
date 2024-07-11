package com.example.attendancemanagement.room_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.attendancemanagement.models.StudentData

@Dao
interface AttendanceDao {
    @Upsert
    suspend fun upsertAttendance(attendance: Attendance)

    @Delete
    suspend fun deleteAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance")
    suspend fun getAllAttendance(): List<Attendance>

    @Upsert
    suspend fun markAttendance(attendance: MarkAttendance)

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

    @Upsert
    suspend fun createAttendance(attendance: Attendance)

    @Query("Select * from STUDENT where classId=:classId")
    suspend fun getAllStudentsByClass(classId: Int):MutableList<Student>

    @Query("Select * from mark_attendance as ma Join student as s On ma.studentId=s.studentId  where ma.studentId=:studentId and s.classId=:classId")
    suspend fun getStudentAttendance(studentId: String,classId: Int):MarkAttendance

    @Query("SELECT \n" +
            "    COUNT(CASE WHEN ma.attendance = 1 THEN ma.markAttendanceId ELSE NULL END) AS attendance,\n" +
            "    COUNT(CASE WHEN ma.attendance = 0 THEN ma.markAttendanceId ELSE NULL END) AS absentees,\n" +
            "    c.classTitle, \n" +
            "    s.sessionTitle\n" +
            "FROM session_class AS c\n" +
            "JOIN session AS s ON s.sessionId = c.sessionId\n" +
            "JOIN (\n" +
            "    SELECT ss.classId, ss.studentId\n" +
            "    FROM student AS ss\n" +
            "    WHERE ss.studentId = :studentId\n" +
            ") AS filtered_students ON filtered_students.classId = c.classId\n" +
            "LEFT JOIN mark_attendance AS ma ON ma.studentId = filtered_students.studentId\n" +
            "GROUP BY c.classTitle, s.sessionTitle\n")
    suspend fun getStudentData(studentId: String): StudentData
}
