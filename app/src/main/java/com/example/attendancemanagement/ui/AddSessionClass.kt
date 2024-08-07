package com.example.attendancemanagement.ui

import StudentListAdapter
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

import com.example.attendancemanagement.models.repositories.AttendanceRepository
import com.example.attendancemanagement.models.repositories.StudentsRepository
import com.example.attendancemanagement.models.User
import com.example.attendancemanagement.room_db.entities.SessionClass
import com.example.attendancemanagement.view_model.AttendanceViewModel
import com.example.attendancemanagement.view_model.StudentsViewModel
import com.example.attendancemanagement.view_model.StudentsViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class AddSessionClass : Fragment(), StudentListAdapter.OnItemClickListener {

    private val classList = mutableListOf<SessionClass>()
    private lateinit var chipGroup: ChipGroup
    private lateinit var studentListAdapter: StudentListAdapter
    private lateinit var studentsViewModel: StudentsViewModel
    private lateinit var attendanceViewModel: AttendanceViewModel
    private var dataList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = StudentsRepository()
        val factory = StudentsViewModelFactory(repository)
        studentsViewModel = ViewModelProvider(this, factory)[StudentsViewModel::class.java]
        attendanceViewModel=AttendanceViewModel(AttendanceRepository(requireContext()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeClassesData()
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
        val sessionEditText=view.findViewById<TextInputEditText>(R.id.sessionTitle)
        val classTitleEditText = view.findViewById<TextInputEditText>(R.id.classTitle)
        val addButton = view.findViewById<AppCompatButton>(R.id.addClassButton)
        val checkButton=view.findViewById<AppCompatButton>(R.id.addSessionButton)

        checkButton.setOnClickListener{
            checkButtonClicked(sessionEditText)
        }

        addButton.setOnClickListener {
            addButtonClicked(classTitleEditText,sessionEditText)
        }

        // Observe LiveData
        observeStudentData()

        setUpAdapter(view)

        return view
    }

    private fun observeStudentData() {
        studentsViewModel.unAssignedStudentsData.observe(viewLifecycleOwner, Observer { data ->
            if (data.isNotEmpty()) {
                dataList.clear()
                dataList.addAll(data)
                studentListAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun observeClassesData() {
        attendanceViewModel.classesBySession.observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                classList.clear()
                classList.addAll(data)
                studentListAdapter.updateClassList(classList)
                studentListAdapter.notifyDataSetChanged()
                updateChipGroup()
            }
        })
    }



    private fun addButtonClicked(classTitleEditText: TextInputEditText,sessionEditText:TextInputEditText) {
        val className = classTitleEditText.text.toString()
        val sessionName = sessionEditText.text.toString()
        if (className.isNotEmpty()) {
            addClassName(className,sessionName)
            classTitleEditText.text?.clear()
        }
    }

    private fun setUpAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.studentsList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        studentListAdapter = StudentListAdapter(dataList, this, classList)
        recyclerView.adapter = studentListAdapter
    }

    private fun addClassName(className: String,sessionName:String) {
        attendanceViewModel.addClass(sessionName, className)
        attendanceViewModel.getClassesBySession(sessionName)
        studentListAdapter.updateClassList(classList)
        studentListAdapter.notifyDataSetChanged()
    }

    private fun removeClassName(sessionClass: SessionClass) {
        attendanceViewModel.removeClass(sessionClass)
        classList.remove(sessionClass)
        studentListAdapter.updateClassList(classList)
        studentListAdapter.notifyDataSetChanged()
    }

    private fun updateChipGroup() {
        chipGroup.removeAllViews()
        for (sessionClass in classList) {
            val chip = Chip(requireContext()).apply {
                text = sessionClass.classTitle
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    removeClassName(sessionClass)
                    chipGroup.removeView(this)
                }
            }
            chipGroup.addView(chip)
        }
    }


    override fun onButtonClick(user: User,classId:Int,isIconClicked:Boolean) {
       if(!isIconClicked){
           val sessionTitle= view?.findViewById<TextInputEditText>(R.id.sessionTitle)?.text.toString()
           attendanceViewModel.addStudentToClass(user, sessionTitle,classId)
           studentsViewModel.updateStudentStatus(user,true)
       }else{
           val sessionTitle= view?.findViewById<TextInputEditText>(R.id.sessionTitle)?.text.toString()
           attendanceViewModel.removeStudentToClass(user, sessionTitle,classId)
           studentsViewModel.updateStudentStatus(user,false)
       }
    }

    private fun checkButtonClicked(sessionText: TextInputEditText) {
        val sessionName = sessionText.text.toString()
        attendanceViewModel.getClassesBySession(sessionName)

        attendanceViewModel.classesBySession.observe(viewLifecycleOwner, Observer { data ->
            if (data != null && data.isNotEmpty()) {
                classList.clear()
                classList.addAll(data)
                studentListAdapter.updateClassList(classList)
                studentListAdapter.notifyDataSetChanged()
                updateChipGroup()
            } else {
                // No classes found, create a new session
                attendanceViewModel.addSession(sessionName)
            }
        })
    }

}
