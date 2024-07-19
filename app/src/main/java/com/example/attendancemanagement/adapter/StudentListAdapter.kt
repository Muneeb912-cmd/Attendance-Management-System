import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagement.R
import com.example.attendancemanagement.models.User
import com.example.attendancemanagement.room_db.entities.SessionClass

class StudentListAdapter(
    private val dataList: List<User>,
    private val listener: OnItemClickListener,
    private var classList: List<SessionClass> // Changed to var for mutability
) : RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onButtonClick(user: User,classId:Int,isIconClicked:Boolean)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView = itemView.findViewById(R.id.Add_studentName)
        val classSelector: Spinner = itemView.findViewById(R.id.classSpinner)
        val addButton: ImageButton = itemView.findViewById(R.id.Add_imageButton)
        var classID:Int=0

        private var isIconClicked = false // Initial state for icon

        init {
            addButton.setOnClickListener(this)
            updateIcon()
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION && v == addButton) {
                listener.onButtonClick(dataList[position],classID,isIconClicked) // Passing user name to listener
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

        fun bind(sessionClassList: List<SessionClass>) {
            val spinnerAdapter = ArrayAdapter(
                itemView.context,
                android.R.layout.simple_spinner_item,
                sessionClassList.map { it.classTitle }
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            classSelector.adapter = spinnerAdapter

            classSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedClass = sessionClassList[position]
                    // Here you can get the selected class id
                    val selectedClassId = selectedClass.classId
                    classID=selectedClassId
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_student_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = dataList[position].userName
        holder.bind(classList)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // Method to update classList
    fun updateClassList(newList: List<SessionClass>) {
        classList = newList
        notifyDataSetChanged()
    }
}
