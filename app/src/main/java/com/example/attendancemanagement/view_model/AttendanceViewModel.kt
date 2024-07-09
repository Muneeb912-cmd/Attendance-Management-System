package com.example.attendancemanagement.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancemanagement.models.AttendanceRepository
import com.example.attendancemanagement.room_db.SessionClass
import kotlinx.coroutines.launch

class AttendanceViewModel(private val repository:AttendanceRepository):ViewModel() {

    private val _classesBySession = MutableLiveData<List<SessionClass>>()
    val classesBySession: LiveData<List<SessionClass>> get() = _classesBySession

    fun addSession(sessionTitle:String)=viewModelScope.launch {
        repository.createSession(sessionTitle)
    }

    fun addClass(sessionTitle: String,classTitle: String)=viewModelScope.launch {
        repository.createClass(sessionTitle,classTitle)
    }

    fun removeClass(sessionClass: SessionClass)=viewModelScope.launch {
        repository.removeClass(sessionClass)
    }



    fun getClassesBySession(sessionTitle: String) = viewModelScope.launch {
        try {
            _classesBySession.value = repository.getAllClassesBySession(sessionTitle)
        } catch (e: Exception) {
            // Handle the exception (e.g., logging, notifying UI)
            // Optionally, set a default or empty list on error
            _classesBySession.value = emptyList()
        }
    }
}