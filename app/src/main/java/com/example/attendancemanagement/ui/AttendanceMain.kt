package com.example.attendancemanagement.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.attendancemanagement.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class AttendanceMain : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_attendance_main, container, false)

        // Initialize DrawerLayout and Toolbar
        drawerLayout = view.findViewById(R.id.attendanceMain)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        // Set up Toolbar
        val activity = activity
        if (activity is AppCompatActivity) {
            activity.setSupportActionBar(toolbar)
        }

        // Initialize NavigationView and set its listener
        val navigationView = view.findViewById<NavigationView>(R.id.nev_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Setup ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        return view
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.home-> Toast.makeText(requireContext(),"Home Clicked",Toast.LENGTH_LONG).show()
            R.id.nav_logout-> logOut()
            R.id.addSessionClass-> navigateToAddSessionClass()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    private fun navigateToAddSessionClass() {
        findNavController().navigate(R.id.action_attendanceMain2_to_addSessionClass2)
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_attendanceMain2_to_login_Signup)

    }
}