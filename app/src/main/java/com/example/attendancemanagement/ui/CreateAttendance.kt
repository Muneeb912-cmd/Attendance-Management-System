package com.example.attendancemanagement.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.attendancemanagement.R
import com.example.attendancemanagement.models.repositories.AttendanceRepository
import com.example.attendancemanagement.room_db.entities.Attendance
import com.example.attendancemanagement.room_db.entities.Session
import com.example.attendancemanagement.room_db.entities.SessionClass
import com.example.attendancemanagement.view_model.AttendanceViewModel
import com.google.android.material.button.MaterialButton
import java.util.Calendar

class CreateAttendance : Fragment() {

    private var sessionList = mutableListOf<Session>()
    private var classList = mutableListOf<SessionClass>()
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var sessionAdapter: ArrayAdapter<String>
    private lateinit var classAdapter: ArrayAdapter<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        attendanceViewModel=AttendanceViewModel(AttendanceRepository(requireContext()))
        attendanceViewModel.getSessions() // Fetch sessions
        val view = inflater.inflate(R.layout.fragment_create_attendance, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.createAttendanceToolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        val createButton: MaterialButton = view.findViewById(R.id.createAttendanceBtn)
        val date: TextView = view.findViewById(R.id.selectedDate)
        val attendanceTitle: EditText = view.findViewById(R.id.attendanceTitle)
        val sessionPicker: AutoCompleteTextView = view.findViewById(R.id.sessionAutoCompleteTextView)
        val classPicker: AutoCompleteTextView = view.findViewById(R.id.classAutoCompleteTextView)
        var selectedSessionId=0
        var selectedClassId=0

        // Setup session adapter
        sessionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        sessionPicker.setAdapter(sessionAdapter)

        // Setup class adapter
        classAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        classPicker.setAdapter(classAdapter)

        // Observe Session Data
        observeSessionData()
        observeClassesData()

        // Handle session selection
        sessionPicker.setOnItemClickListener { _, _, position, _ ->
            val selectedSession = sessionList[position]
            attendanceViewModel.getClassesBySession(selectedSession.sessionTitle)
            selectedSessionId=selectedSession.sessionId
        }

        // Handle class selection
        classPicker.setOnItemClickListener { _, _, position, _ ->
            val selectedClass = classList[position]
            selectedClassId = selectedClass.classId // Get the ID of the selected class
            // Use the selectedClassId as needed
        }

        createButton.setOnClickListener {
            onCreateButtonClicked(date,attendanceTitle,selectedSessionId,selectedClassId)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<MaterialButton>(R.id.pickDate)?.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    val date = "$dayOfMonth-${monthOfYear + 1}-$year"
                    view.findViewById<TextView?>(R.id.selectedDate)?.text = date
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    private fun observeSessionData() {
        attendanceViewModel.sessions.observe(viewLifecycleOwner, Observer { data ->
            sessionList.clear()
            sessionList.addAll(data)
            sessionAdapter.clear()
            sessionAdapter.addAll(data.map { it.sessionTitle })
            sessionAdapter.notifyDataSetChanged()
        })
    }

    private fun observeClassesData() {
        attendanceViewModel.classesBySession.observe(viewLifecycleOwner, Observer { data ->
            classList.clear()
            classList.addAll(data)
            classAdapter.clear()
            classAdapter.addAll(data.map { it.classTitle })
            classAdapter.notifyDataSetChanged()
        })
    }

    private fun onCreateButtonClicked(
        date: TextView,
        attendanceTitle: EditText,
        selectedSessionId: Int,
        selectedClassId: Int,

        ) {
        val attendance= Attendance(
            dateCreated = date.text.toString(),
            attendanceTitle = attendanceTitle.text.toString(),
            sessionId = selectedSessionId,
            classId = selectedClassId
        )

        attendanceViewModel.createAttendance(attendance)
        Toast.makeText(requireContext(),"Attendance Created Successfully",Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressed()
    }
}
