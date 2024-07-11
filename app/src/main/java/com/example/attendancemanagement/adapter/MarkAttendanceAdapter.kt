package com.example.attendancemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.attendancemanagement.room_db.MarkAttendance
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagement.R
import com.example.attendancemanagement.models.AttendanceRepository
import com.example.attendancemanagement.room_db.Student
import com.example.attendancemanagement.view_model.AttendanceViewModel

class MarkAttendanceAdapter(
    private val dataList: MutableList<Student>,
    private val listener: OnItemClickListener,
    context: Context
) : RecyclerView.Adapter<MarkAttendanceAdapter.ViewHolderClass>() {

    private var attendanceViewModel=AttendanceViewModel(AttendanceRepository(context))
    interface OnItemClickListener {
        fun onSwitchToggle(position: Int, isChecked: Boolean)
    }

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studnetId = itemView.findViewById<TextView>(R.id.studentID)
        val studentName = itemView.findViewById<TextView>(R.id.studentName)
        val attendanceToggle = itemView.findViewById<SwitchCompat>(R.id.attendanceToggle)

        init {
            attendanceToggle.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onSwitchToggle(position, isChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarkAttendanceAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.mark_attendance_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        val index = position + 1
        holder.studnetId.text = index.toString()
        holder.studentName.text = currentItem.studentName
        attendanceViewModel.getStudentAttendance(currentItem.studentId,currentItem.classId){
            if (it != null) {
                holder.attendanceToggle.isChecked= it.attendance
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}