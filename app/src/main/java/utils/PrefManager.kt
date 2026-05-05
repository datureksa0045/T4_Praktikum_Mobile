package utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun saveLoginSession(isLoggedIn: Boolean, username: String) {
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("username", username)
        editor.apply()
    }

    fun isLoggedIn(): Boolean = pref.getBoolean("isLoggedIn", false)
    fun getUsername(): String? = pref.getString("username", "User")
    fun setRememberMe(status: Boolean) = editor.putBoolean("rememberMe", status).apply()
    fun isRememberMe(): Boolean = pref.getBoolean("rememberMe", false)

    fun logout() {
        editor.clear().apply()
    }
}