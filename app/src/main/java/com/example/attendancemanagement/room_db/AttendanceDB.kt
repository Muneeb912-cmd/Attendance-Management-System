package com.example.attendancemanagement.room_db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.attendancemanagement.room_db.entities.Attendance
import com.example.attendancemanagement.room_db.entities.MarkAttendance
import com.example.attendancemanagement.room_db.entities.Session
import com.example.attendancemanagement.room_db.entities.SessionClass
import com.example.attendancemanagement.room_db.entities.Student

@Database(
    entities = [
        Attendance::class,
        Student::class,
        MarkAttendance::class,
        Session::class,
        SessionClass::class,
    ],
    version = 9
)
abstract class AttendanceDB : RoomDatabase() {
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile private var INSTANCE: AttendanceDB? = null

        fun getInstance(context: Context): AttendanceDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AttendanceDB::class.java,
                    "AttendanceDB"
                )
                    .fallbackToDestructiveMigration() // Use this only during development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
