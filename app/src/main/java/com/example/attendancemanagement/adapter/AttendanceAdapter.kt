package com.example.attendancemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagement.R
import com.example.attendancemanagement.room_db.entities.Attendance

class AttendanceAdapter(
    private val dataList: MutableList<Attendance>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AttendanceAdapter.ViewHolderClass>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
            val attendanceId: TextView=itemView.findViewById(R.id.AttendanceIndex)
        val attendanceTitle: TextView=itemView.findViewById(R.id.attendanceTitle)
        val createdOn: TextView=itemView.findViewById(R.id.createdOn)


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttendanceAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.attendance_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: AttendanceAdapter.ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.attendanceId.text=currentItem.attendanceId.toString()
        holder.attendanceTitle.text=currentItem.attendanceTitle
        val createdOn="Date: ${currentItem.dateCreated}"
        holder.createdOn.text=createdOn
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}