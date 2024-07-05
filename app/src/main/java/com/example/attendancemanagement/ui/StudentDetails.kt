package com.example.attendancemanagement.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.attendancemanagement.R
import com.google.firebase.auth.FirebaseAuth

class StudentDetails : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_student_details, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar1)
        toolbar.setNavigationOnClickListener {
            logOut()
        }
        return view
    }
    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_studentDetails_to_login_Signup)

    }
}