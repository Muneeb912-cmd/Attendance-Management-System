package com.example.attendancemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagement.R
import com.example.attendancemanagement.models.User

class StudentListAdapter(
    private val dataList: List<User>,
    private val listener: OnItemClickListener,
    private var classList: List<String> // Changed to var for mutability
) : RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onButtonClick(user: User)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView = itemView.findViewById(R.id.Add_studentName)
        val classSelector: Spinner = itemView.findViewById(R.id.classSpinner)
        val addButton: ImageButton = itemView.findViewById(R.id.Add_imageButton)

        private var isIconClicked = false // Initial state for icon

        init {
            addButton.setOnClickListener(this)
            updateIcon()
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION && v == addButton) {
                listener.onButtonClick(dataList[position]) // Passing user name to listener
                isIconClicked = !isIconClicked // Toggle state
                updateIcon() // Update icon after click
            }
        }

        private fun updateIcon() {
            if (isIconClicked) {
                addButton.setImageResource(R.drawable.ic_delete)
            } else {
                addButton.setImageResource(R.drawable.ic_add)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_student_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = dataList[position].userName
        // Update Spinner (classSelector) with current classList
        val adapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, classList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.classSelector.adapter = adapter
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // Method to update classList
    fun updateClassList(newList: List<String>) {
        classList = newList
        notifyDataSetChanged()
    }
}
