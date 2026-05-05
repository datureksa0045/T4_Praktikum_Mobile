package com.example.studentcontactapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.entity.StudentEntity
import kotlinx.coroutines.launch

class AddEditStudentActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etNim: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSemester: EditText
    private lateinit var spinnerProdi: Spinner
    private lateinit var btnSave: Button
    private lateinit var tvTitle: TextView
    private lateinit var btnBack: ImageButton

    private var studentId: Int = 0
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_student)

        etName = findViewById(R.id.etName)
        etNim = findViewById(R.id.etNim)
        etEmail = findViewById(R.id.etEmail)
        etSemester = findViewById(R.id.etSemester)
        spinnerProdi = findViewById(R.id.spinnerProdi)
        btnSave = findViewById(R.id.btnSave)
        tvTitle = findViewById(R.id.tvTitle)
        btnBack = findViewById(R.id.btnBack)

        // Setup Spinner
        val prodiOptions = arrayOf("T. Informatika", "Sistem Informasi", "Teknik Elektro", "Teknik Sipil")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prodiOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProdi.adapter = adapter

        // Check if edit mode
        val student = intent.getSerializableExtra("STUDENT_DATA") as? StudentEntity
        if (student != null) {
            isEditMode = true
            studentId = student.id
            tvTitle.text = "Edit Mahasiswa"
            etName.setText(student.name)
            etNim.setText(student.nim)
            etEmail.setText(student.email)
            etSemester.setText(student.semester)
            val spinnerPosition = prodiOptions.indexOf(student.prodi)
            if (spinnerPosition != -1) spinnerProdi.setSelection(spinnerPosition)
        }

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            saveStudent()
        }
    }

    private fun saveStudent() {
        val name = etName.text.toString().trim()
        val nim = etNim.text.toString().trim()
        val prodi = spinnerProdi.selectedItem.toString()
        val email = etEmail.text.toString().trim()
        val semester = etSemester.text.toString().trim()

        if (name.isEmpty() || nim.isEmpty() || email.isEmpty() || semester.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field!", Toast.LENGTH_SHORT).show()
            return
        }

        val student = StudentEntity(
            id = studentId,
            name = name,
            nim = nim,
            prodi = prodi,
            email = email,
            semester = semester
        )

        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            if (isEditMode) {
                db.studentDao().update(student)
                Toast.makeText(this@AddEditStudentActivity, "Data diperbarui", Toast.LENGTH_SHORT).show()
            } else {
                db.studentDao().insert(student)
                Toast.makeText(this@AddEditStudentActivity, "Data ditambahkan", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
