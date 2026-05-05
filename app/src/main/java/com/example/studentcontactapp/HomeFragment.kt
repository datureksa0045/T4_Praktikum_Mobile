package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcontactapp.adapter.StudentAdapter
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.entity.StudentEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var rvStudents: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var adapter: StudentAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        db = AppDatabase.getDatabase(requireContext())
        rvStudents = view.findViewById(R.id.rvStudents)
        fabAdd = view.findViewById(R.id.fabAdd)

        setupRecyclerView()

        fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddEditStudentActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter(
            students = emptyList(),
            onEditClick = { student ->
                val intent = Intent(requireContext(), AddEditStudentActivity::class.java)
                intent.putExtra("STUDENT_DATA", student)
                startActivity(intent)
            },
            onDeleteClick = { student ->
                showDeleteDialog(student)
            },
            onItemClick = { student ->
                val intent = Intent(requireContext(), DetailMahasiswaActivity::class.java)
                intent.putExtra("STUDENT_DATA", student)
                startActivity(intent)
            }
        )
        rvStudents.layoutManager = LinearLayoutManager(requireContext())
        rvStudents.adapter = adapter
    }

    private fun showDeleteDialog(student: StudentEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Data?")
            .setMessage("Hapus \"${student.name}\"? Tindakan ini tidak dapat dibatalkan.")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    db.studentDao().deleteById(student.id)
                    loadData()
                    Toast.makeText(context, "Data dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            val students = db.studentDao().getAllStudents()
            
            // Insert sample data if empty
            if (students.isEmpty()) {
                val sampleData = listOf(
                    StudentEntity(name = "Ahmad Fauzi", nim = "2024001", prodi = "T. Informatika", email = "ahmad@mail.com", semester = "6"),
                    StudentEntity(name = "Budi Santoso", nim = "2024002", prodi = "Sistem Informasi", email = "budi@mail.com", semester = "4"),
                    StudentEntity(name = "Clara Wijaya", nim = "2024003", prodi = "Teknik Elektro", email = "clara@mail.com", semester = "2")
                )
                db.studentDao().insertAll(sampleData)
                adapter.updateData(db.studentDao().getAllStudents())
            } else {
                adapter.updateData(students)
            }
        }
    }
}
