package com.example.attendancemanagement.view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.attendancemanagement.models.repositories.StudentsRepository

class StudentsViewModelFactory(private val repository: StudentsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}