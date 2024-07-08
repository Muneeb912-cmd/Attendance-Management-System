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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.attendancemanagement.R
import com.example.attendancemanagement.models.User

import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth

class StudentDetails : Fragment() {

    private lateinit var userData: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         //Retrieve arguments
        arguments?.let {
            userData = StudentDetailsArgs.fromBundle(it).userData
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_student_details, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar1)
        toolbar.setNavigationOnClickListener {
            logOut()
        }

        view.findViewById<TextView>(R.id.Details_Name).text=userData.userName
        view.findViewById<TextView>(R.id.Details_Email).text=userData.userEmail
        Glide.with(requireContext())
            .load(userData.userPhoto)
            .into(view.findViewById(R.id.Details_Image))
        val id="ID: "+userData.userId
        view.findViewById<TextView>(R.id.Details_ID).text=id


        return view
    }
    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_studentDetails_to_login_Signup)

    }
}