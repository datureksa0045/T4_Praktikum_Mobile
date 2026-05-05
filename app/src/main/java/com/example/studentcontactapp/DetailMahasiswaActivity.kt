package com.example.studentcontactapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentcontactapp.database.entity.StudentEntity
import utils.FileHelper

class DetailMahasiswaActivity : AppCompatActivity() {

    private lateinit var etNote: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLoad: Button
    private lateinit var tvStatus: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var tvName: TextView
    private lateinit var tvNimJurusan: TextView
    private lateinit var tvAvatar: TextView

    private var studentNim: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mahasiswa)

        etNote = findViewById(R.id.etNote)
        btnSave = findViewById(R.id.btnSaveNote)
        btnLoad = findViewById(R.id.btnLoadNote)
        tvStatus = findViewById(R.id.tvNoteStatus)
        btnBack = findViewById(R.id.btnBack)
        tvName = findViewById(R.id.tvDetailName)
        tvNimJurusan = findViewById(R.id.tvDetailNimJurusan)
        tvAvatar = findViewById(R.id.tvAvatarInitial)

        // Ambil data dari Intent
        val student = intent.getSerializableExtra("STUDENT_DATA") as? StudentEntity
        if (student != null) {
            studentNim = student.nim
            tvName.text = student.name
            tvNimJurusan.text = "${student.nim} • ${student.prodi}"
            
            // Update Avatar Initial
            tvAvatar.text = if (student.name.isNotEmpty()) {
                val names = student.name.split(" ")
                if (names.size >= 2) {
                    (names[0].take(1) + names[1].take(1)).uppercase()
                } else {
                    student.name.take(2).uppercase()
                }
            } else "?"
        } else {
            // Fallback for NIM only
            val nim = intent.getStringExtra("STUDENT_NIM")
            if (nim != null) {
                studentNim = nim
                tvNimJurusan.text = "$studentNim • Mahasiswa"
            }
        }

        btnBack.setOnClickListener {
            finish()
        }

        // A. Muat Catatan secara otomatis saat halaman dibuka
        if (studentNim.isNotEmpty()) {
            val existingNote = FileHelper.loadNote(this, studentNim)
            if (existingNote.isNotEmpty()) {
                etNote.setText(existingNote)
            }
            refreshNoteStatus()
        }

        // B. Tombol Simpan
        btnSave.setOnClickListener {
            val content = etNote.text.toString()
            if (studentNim.isEmpty()) {
                Toast.makeText(this, "Data mahasiswa tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (content.isNotEmpty()) {
                FileHelper.saveNote(this, studentNim, content)
                refreshNoteStatus()
                Toast.makeText(this, "Catatan disimpan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Catatan kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        // C. Tombol Muat (Manual)
        btnLoad.setOnClickListener {
            if (studentNim.isEmpty()) return@setOnClickListener
            
            val content = FileHelper.loadNote(this, studentNim)
            if (content.isNotEmpty()) {
                etNote.setText(content)
                refreshNoteStatus()
                Toast.makeText(this, "Catatan berhasil dimuat", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Belum ada catatan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshNoteStatus() {
        if (studentNim.isEmpty()) return

        if (FileHelper.isNoteExists(this, studentNim)) {
            val size = FileHelper.getFileSize(this, studentNim)
            tvStatus.text = "✓ Tersimpan ($size bytes)"
            tvStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
        } else {
            tvStatus.text = "Belum ada catatan"
            tvStatus.setTextColor(resources.getColor(android.R.color.darker_gray, null))
        }
    }
}
