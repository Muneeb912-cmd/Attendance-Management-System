package com.example.attendancemanagement.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagement.R
import com.example.attendancemanagement.adapter.MarkAttendanceAdapter
import com.example.attendancemanagement.models.repositories.AttendanceRepository
import com.example.attendancemanagement.room_db.entities.Student
import com.example.attendancemanagement.room_db.entities.MarkAttendance
import com.example.attendancemanagement.view_model.AttendanceViewModel


class MarkAttendance : Fragment(), MarkAttendanceAdapter.OnItemClickListener {

    private val students = mutableListOf<Student>()
    private val studentsAttendance = mutableListOf<MarkAttendance>()
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var markAttendanceAdapter: MarkAttendanceAdapter
    private var classId: Int = 0
    private var attendanceId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attendanceViewModel = AttendanceViewModel(AttendanceRepository(requireContext()))
        arguments?.let {
            classId = MarkAttendanceArgs.fromBundle(it).classId
            attendanceId = MarkAttendanceArgs.fromBundle(it).attendanceId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mark_attendance, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.markAttendanceToolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        attendanceViewModel.getStudentsByClass(classId)
        attendanceViewModel.getAllAttendance()

        observeStudentData()
        observeStudentAttendanceData()

        setUpAdapter(view)

        return view
    }

    private fun observeStudentAttendanceData() {
        attendanceViewModel.studentAttendance.observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                studentsAttendance.clear()
                studentsAttendance.addAll(data)
                markAttendanceAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun observeStudentData() {
        attendanceViewModel.students.observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                students.clear()
                students.addAll(data)
                markAttendanceAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun setUpAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.markAttendanceRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        markAttendanceAdapter = MarkAttendanceAdapter(students, this,requireContext())
        recyclerView.adapter = markAttendanceAdapter
    }

    override fun onSwitchToggle(position: Int, isChecked: Boolean) {

        val studentAttendance = studentsAttendance.getOrNull(0)

        val attendance = if (isChecked) {
            if (studentAttendance?.markAttendanceId != null) {
                // Update existing attendance
                MarkAttendance(
                    isChecked,
                    attendanceId,
                    students[position].studentId,
                    studentAttendance.markAttendanceId
                )
            } else {
                // Create new attendance
                MarkAttendance(
                    isChecked,
                    attendanceId,
                    students[position].studentId
                )
            }
        } else {
            // Uncheck (update existing attendance)
            MarkAttendance(
                isChecked,
                attendanceId,
                students[position].studentId,
                studentAttendance?.markAttendanceId ?: 0  // Provide a default value here as per your logic
            )
        }

        attendanceViewModel.markAttendance(attendance)
    }

}