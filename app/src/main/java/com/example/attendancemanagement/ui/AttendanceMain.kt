package com.example.attendancemanagement.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagement.R
import com.example.attendancemanagement.adapter.AttendanceAdapter
import com.example.attendancemanagement.models.AttendanceRepository
import com.example.attendancemanagement.models.StudentsRepository
import com.example.attendancemanagement.models.User
import com.example.attendancemanagement.room_db.Attendance
import com.example.attendancemanagement.room_db.SessionClass
import com.example.attendancemanagement.room_db.Student
import com.example.attendancemanagement.view_model.AttendanceViewModel
import com.example.attendancemanagement.view_model.StudentsViewModel
import com.example.attendancemanagement.view_model.StudentsViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class AttendanceMain : Fragment(), NavigationView.OnNavigationItemSelectedListener,
    AttendanceAdapter.OnItemClickListener {

    private lateinit var drawerLayout: DrawerLayout
    private val attendanceList = mutableListOf<Attendance>()
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var studentsViewModel: StudentsViewModel
    private lateinit var attendanceAdapter: AttendanceAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = StudentsRepository()
        val factory = StudentsViewModelFactory(repository)
        attendanceViewModel = AttendanceViewModel(AttendanceRepository(requireContext()))
        studentsViewModel = ViewModelProvider(this, factory)[StudentsViewModel::class.java]
    }

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

        attendanceViewModel.getAllAttendance()

        observeAttendanceData()
        setUpAdapter(view)

        return view
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.home -> Toast.makeText(requireContext(), "Home Clicked", Toast.LENGTH_LONG).show()
            R.id.nav_logout -> logOut()
            R.id.addSessionClass -> navigateToAddSessionClass()
            R.id.addAttendance -> addAttendance()
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

    private fun addAttendance() {
        findNavController().navigate(R.id.action_attendanceMain2_to_createAttendance)
    }

    private fun observeAttendanceData() {
        attendanceViewModel.attendance.observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                attendanceList.clear()
                attendanceList.addAll(data)
                attendanceAdapter.notifyDataSetChanged()
            }
        })
    }



    private fun setUpAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.attendanceRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        attendanceAdapter = AttendanceAdapter(attendanceList, this)
        recyclerView.adapter = attendanceAdapter
    }

    override fun onItemClick(position: Int) {
        val attendanceId = attendanceList[position].attendanceId
        val classId = attendanceList[position].classId
        val action = AttendanceMainDirections.actionAttendanceMain2ToMarkAttendance(classId,attendanceId)
        findNavController().navigate(action)

    }
}