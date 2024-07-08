package com.example.attendancemanagement.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagement.R
import com.example.attendancemanagement.adapter.StudentListAdapter
import com.example.attendancemanagement.models.SessionClassStudent
import com.example.attendancemanagement.models.StudentsRepository
import com.example.attendancemanagement.models.User
import com.example.attendancemanagement.view_model.StudentsViewModel
import com.example.attendancemanagement.view_model.StudentsViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class AddSessionClass : Fragment(), StudentListAdapter.OnItemClickListener {

    private val classList = mutableListOf<String>()
    private lateinit var chipGroup: ChipGroup
    private lateinit var studentListAdapter: StudentListAdapter
    private lateinit var studentsViewModel: StudentsViewModel
    private var dataList = ArrayList<User>()

    private var sessionClass=ArrayList<SessionClassStudent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = StudentsRepository()
        val factory = StudentsViewModelFactory(repository)
        studentsViewModel = ViewModelProvider(this, factory)[StudentsViewModel::class.java]
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_session_class, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.addSessionClassToolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        chipGroup = view.findViewById(R.id.chipGroup)
        val classTitleEditText = view.findViewById<TextInputEditText>(R.id.classTitle)
        val addButton = view.findViewById<AppCompatButton>(R.id.addClassButton)

        addButton.setOnClickListener {
            addButtonClicked(classTitleEditText)
        }

        // Observe LiveData
        observeStudentData()

        setUpAdapter(view)

        return view
    }

    private fun observeStudentData() {
        studentsViewModel.studentsData.observe(viewLifecycleOwner, Observer { data ->
            if (data.isNotEmpty()) {
                dataList.clear()
                dataList.addAll(data)
                studentListAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun addButtonClicked(classTitleEditText: TextInputEditText) {
        val className = classTitleEditText.text.toString()
        if (className.isNotEmpty()) {
            addClassName(className)
            classTitleEditText.text?.clear()
        }
    }

    private fun setUpAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.studentsList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        studentListAdapter = StudentListAdapter(dataList, this, classList)
        recyclerView.adapter = studentListAdapter
    }

    private fun addClassName(className: String) {
        classList.add(className)
        updateChipGroup()
        studentListAdapter.notifyDataSetChanged()
    }

    private fun updateChipGroup() {
        chipGroup.removeAllViews()
        for (className in classList) {
            val chip = Chip(requireContext()).apply {
                text = className
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    classList.remove(className)
                    chipGroup.removeView(this)
                    studentListAdapter.notifyDataSetChanged()
                }
            }
            chipGroup.addView(chip)
        }
    }

    override fun onButtonClick(user: User) {
        
    }
}
