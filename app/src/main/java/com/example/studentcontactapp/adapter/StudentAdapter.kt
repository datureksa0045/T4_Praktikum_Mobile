package com.example.studentcontactapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcontactapp.R
import com.example.studentcontactapp.database.entity.StudentEntity

class StudentAdapter(
    private var students: List<StudentEntity>,
    private val onEditClick: (StudentEntity) -> Unit,
    private val onDeleteClick: (StudentEntity) -> Unit,
    private val onItemClick: (StudentEntity) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    fun updateData(newStudents: List<StudentEntity>) {
        students = newStudents
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    override fun getItemCount(): Int = students.size

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvInitial: TextView = itemView.findViewById(R.id.tvInitial)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvNim: TextView = itemView.findViewById(R.id.tvNim)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(student: StudentEntity) {
            tvName.text = student.name
            tvNim.text = student.nim
            tvInitial.text = if (student.name.isNotEmpty()) {
                val names = student.name.split(" ")
                if (names.size >= 2) {
                    (names[0].take(1) + names[1].take(1)).uppercase()
                } else {
                    student.name.take(2).uppercase()
                }
            } else "?"

            itemView.setOnClickListener { onItemClick(student) }
            btnEdit.setOnClickListener { onEditClick(student) }
            btnDelete.setOnClickListener { onDeleteClick(student) }
        }
    }
}
