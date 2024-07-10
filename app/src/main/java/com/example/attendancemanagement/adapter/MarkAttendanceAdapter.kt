package com.example.attendancemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.attendancemanagement.room_db.MarkAttendance
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagement.R
import com.example.attendancemanagement.room_db.Student

class MarkAttendanceAdapter(
private val dataList: MutableList<Student>,
) : RecyclerView.Adapter<MarkAttendanceAdapter.ViewHolderClass>() {

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView){
        val studnetId=itemView.findViewById<TextView>(R.id.studentID)
        val studentName=itemView.findViewById<TextView>(R.id.studentName)
        val attendanceTooggle=itemView.findViewById<SwitchCompat>(R.id.attendanceToggle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarkAttendanceAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mark_attendance_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.studnetId.text=currentItem.studentId
        holder.studentName.text=currentItem.studentName
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}