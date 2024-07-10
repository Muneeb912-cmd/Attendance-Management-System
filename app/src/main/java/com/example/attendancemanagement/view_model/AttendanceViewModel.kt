package com.example.attendancemanagement.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancemanagement.models.AttendanceRepository
import com.example.attendancemanagement.models.User
import com.example.attendancemanagement.room_db.Attendance
import com.example.attendancemanagement.room_db.Session
import com.example.attendancemanagement.room_db.SessionClass
import com.example.attendancemanagement.room_db.Student
import kotlinx.coroutines.launch

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    private val _classesBySession = MutableLiveData<List<SessionClass>>()
    private val _session = MutableLiveData<List<Session>>()
    private val _attendance = MutableLiveData<List<Attendance>>()
    private val _students = MutableLiveData<MutableList<Student>>()
    val students: LiveData<MutableList<Student>> get() = _students

    val classesBySession: LiveData<List<SessionClass>> get() = _classesBySession
    val sessions: LiveData<List<Session>> get() = _session
    val attendance: LiveData<List<Attendance>> get() = _attendance

    fun addSession(sessionTitle: String) = viewModelScope.launch {
        repository.createSession(sessionTitle)
        getSessions() // Refresh sessions after adding
    }

    fun addClass(sessionTitle: String, classTitle: String) = viewModelScope.launch {
        repository.createClass(sessionTitle, classTitle)
        getClassesBySession(sessionTitle) // Refresh classes after adding
    }

    fun removeClass(sessionClass: SessionClass) = viewModelScope.launch {
        repository.removeClass(sessionClass)
    }

    fun getClassesBySession(sessionTitle: String) = viewModelScope.launch {
        try {
            _classesBySession.value = repository.getAllClassesBySession(sessionTitle)
        } catch (e: Exception) {
            _classesBySession.value = emptyList()
        }
    }

    fun getSessions() = viewModelScope.launch {
        try {
            _session.value = repository.getAllSessions()
        } catch (e: Exception) {
            _session.value = emptyList()
        }
    }

    fun addStudentToClass(user: User, sessionTitle: String, classId: Int) = viewModelScope.launch {
        repository.addStudentToClass(user = user, sessionTitle = sessionTitle, classId = classId)
    }

    fun removeStudentToClass(user: User, sessionTitle: String, classId: Int) = viewModelScope.launch {
        repository.removeStudentToClass(user = user, sessionTitle = sessionTitle, classId = classId)
    }

    fun createAttendance(attendance:Attendance)=viewModelScope.launch{
        repository.createAttendance(attendance)
    }

    fun getAllAttendance()=viewModelScope.launch{
        try {
            _attendance.value = repository.getAllAttendance()
        } catch (e: Exception) {
            _attendance.value = emptyList()
        }
    }


    fun getStudentsByClass(classId: Int) {
        viewModelScope.launch {
            val studentsList = repository.getStudentsByClass(classId)
            _students.postValue(studentsList)
        }
    }
}
