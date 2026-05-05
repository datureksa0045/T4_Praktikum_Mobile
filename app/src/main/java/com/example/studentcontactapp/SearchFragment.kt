package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var etSearch: TextInputEditText
    private lateinit var rvSearch: RecyclerView
    private lateinit var adapter: StudentAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        db = AppDatabase.getDatabase(requireContext())
        etSearch = view.findViewById(R.id.etSearch)
        rvSearch = view.findViewById(R.id.rvSearch)

        setupRecyclerView()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchData(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        searchData("") // Load all initially

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
        rvSearch.layoutManager = LinearLayoutManager(requireContext())
        rvSearch.adapter = adapter
    }

    private fun showDeleteDialog(student: StudentEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Data?")
            .setMessage("Hapus \"${student.name}\"? Tindakan ini tidak dapat dibatalkan.")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    db.studentDao().deleteById(student.id)
                    searchData(etSearch.text.toString())
                    Toast.makeText(context, "Data dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun searchData(query: String) {
        lifecycleScope.launch {
            val results = if (query.isEmpty()) {
                db.studentDao().getAllStudents()
            } else {
                db.studentDao().searchStudents(query)
            }
            adapter.updateData(results)
        }
    }
}
