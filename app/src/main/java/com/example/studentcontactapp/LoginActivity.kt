package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import utils.PrefManager

class LoginActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        prefManager = PrefManager(this)

        if (prefManager.isRememberMe() && prefManager.isLoggedIn()) {
            moveToMainActivity()
        }

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val user = findViewById<EditText>(R.id.etUsername).text.toString()
            val pass = findViewById<EditText>(R.id.etPassword).text.toString()

            if (user == "admin" && pass == "123456") {
                prefManager.setRememberMe(findViewById<CheckBox>(R.id.cbRememberMe).isChecked)
                prefManager.saveLoginSession(true, user)
                moveToMainActivity()
            } else {
                Toast.makeText(this, "Salah!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}