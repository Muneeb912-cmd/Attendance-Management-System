package com.example.attendancemanagement.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.attendancemanagement.R
import com.example.attendancemanagement.models.AttendanceRepository
import com.example.attendancemanagement.models.StudentsRepository
import com.example.attendancemanagement.models.User
import com.example.attendancemanagement.view_model.AttendanceViewModel
import com.example.attendancemanagement.view_model.StudentsViewModel
import com.example.attendancemanagement.view_model.StudentsViewModelFactory

import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth

class StudentDetails : Fragment() {

    private lateinit var userData: User
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var studentViewModel: StudentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        attendanceViewModel = AttendanceViewModel(AttendanceRepository(requireContext()))
        super.onCreate(savedInstanceState)
        val repository = StudentsRepository()
        val factory = StudentsViewModelFactory(repository)
        studentViewModel = ViewModelProvider(this, factory)[StudentsViewModel::class.java]

        arguments?.let {
            userData = StudentDetailsArgs.fromBundle(it).userData
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_student_details, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar1)
        toolbar.setNavigationOnClickListener {
            logOut()
        }



        studentViewModel.getStudentByEmail(userData.userEmail).observe(viewLifecycleOwner) { user ->
            // Update UI or perform actions with user data
            if (user != null) {
                attendanceViewModel.getStudentData(user.userId) {
                    if (it != null) {
                        view.findViewById<TextView>(R.id.studentClass).text = it.classTitle
                        val sessionCount = "Session: " + it.sessionTitle
                        view.findViewById<TextView>(R.id.StudentSession).text = sessionCount
                        val attendanceCount = "Attendance: " + it.attendance.toString()
                        view.findViewById<TextView>(R.id.studentAttendanceCount).text =
                            attendanceCount
                        val absenteesCount = "Absentees: " + it.absentees.toString()
                        view.findViewById<TextView>(R.id.studentAbsentees).text =
                            absenteesCount
                    }
                }
            }
        }




        view.findViewById<TextView>(R.id.Details_Name).text = userData.userName
        view.findViewById<TextView>(R.id.Details_Email).text = userData.userEmail
        Glide.with(requireContext())
            .load(userData.userPhoto)
            .into(view.findViewById(R.id.Details_Image))
        val id = "ID: " + userData.userId
        view.findViewById<TextView>(R.id.Details_ID).text = id

        return view
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_studentDetails_to_login_Signup)

    }

}