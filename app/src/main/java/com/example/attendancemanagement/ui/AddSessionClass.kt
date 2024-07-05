package com.example.attendancemanagement.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.attendancemanagement.R
import com.google.android.material.navigation.NavigationView


class AddSessionClass : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_add_session_class, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.addSessionClassToolbar)
        toolbar.setNavigationOnClickListener {
            // Handle navigation icon click here
            requireActivity().onBackPressed() // Navigate back
        }
        return view
    }
}