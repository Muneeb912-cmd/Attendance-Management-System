package com.example.attendancemanagement.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancemanagement.models.StudentsRepository
import com.example.attendancemanagement.models.User
import kotlinx.coroutines.launch

class StudentsViewModel(private val repository: StudentsRepository):ViewModel() {
    val unAssignedStudentsData: LiveData<List<User>> = repository.getUnAssignedStudents()
    val allStudentsData: LiveData<List<User>> = repository.getRegisteredStudents()

    fun addUser(student: User) = viewModelScope.launch {
        repository.addUser(student)
    }

    fun updateStudentStatus(student: User,status:Boolean)=viewModelScope.launch {
        repository.updateStudentStatus(student,status)
    }

    fun getStudentByEmail(studentEmail: String): LiveData<User?> {
        return repository.getStudentByEmail(studentEmail)
    }

}