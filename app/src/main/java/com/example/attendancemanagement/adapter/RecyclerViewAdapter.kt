package com.example.attendancemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagement.R
import com.example.attendancemanagement.models.Attendance
import com.example.attendancemanagement.models.StudentData
import org.w3c.dom.Text

class RecyclerView(private val context: Context, private val items: MutableList<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val attendaceId=itemView.findViewById<TextView>(R.id.attendanceIndex)
        val attendanceTitle=itemView.findViewById<TextView>(R.id.attendanceTitle)
        val attendanceCreatedOn=itemView.findViewById<TextView>(R.id.createdOn)
    }

    class MarkAttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sdId=itemView.findViewById<TextView>(R.id.studentID)
        val sdName=itemView.findViewById<TextView>(R.id.studentName)
        val sdAttendance=itemView.findViewById<SwitchCompat>(R.id.attendanceToggle)
    }

    class StudentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sdId=itemView.findViewById<TextView>(R.id.studentItemId)
        val sdName=itemView.findViewById<TextView>(R.id.studentItemName)
        val sdSession=itemView.findViewById<TextView>(R.id.studentItemSession)
        val sdClass=itemView.findViewById<TextView>(R.id.studentItemClass)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                AttendanceViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.attendance_item, parent, false)
                )
            }
            2 -> {
                MarkAttendanceViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.mark_attendance_item,parent,false)
                )
            }
            else -> {
                StudentsViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.students_item, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is AttendanceViewHolder -> 0
            is StudentsViewHolder -> 1
            else -> 2
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val currentItem = items[position] as Attendance
            val holder1 = holder as AttendanceViewHolder


            holder1.attendanceTitle.text= currentItem.attendanceTitle
            holder1.attendaceId.text= currentItem.attendanceID.toString()
            holder1.attendanceCreatedOn.text= currentItem.dateCreated

        } else if (getItemViewType(position)==1) {
            val currentItem = items[position] as StudentData
            val holder2 = holder as MarkAttendanceViewHolder

            holder.sdId.text=currentItem.sdId.toString()
            holder.sdName.text=currentItem.name
            holder.sdAttendance.isChecked=currentItem.sdAttendance


        }else{
            val currentItem=items[position] as StudentData
            val holder3 =holder as StudentsViewHolder

            holder3.sdId.text=currentItem.sdId.toString()
            holder3.sdName.text=currentItem.name
            holder3.sdClass.text=currentItem.className
            holder3.sdSession.text=currentItem.sessionName
        }
    }
}