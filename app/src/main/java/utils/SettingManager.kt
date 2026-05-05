package utils

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    companion object {
        private const val KEY_DARK_MODE = "darkMode"
        private const val KEY_NOTIF_ENABLED = "notifEnabled"
    }

    var isDarkMode: Boolean
        get() = sharedPref.getBoolean(KEY_DARK_MODE, false)
        set(value) {
            editor.putBoolean(KEY_DARK_MODE, value).apply()
        }

    var isNotificationEnabled: Boolean
        get() = sharedPref.getBoolean(KEY_NOTIF_ENABLED, true)
        set(value) {
            editor.putBoolean(KEY_NOTIF_ENABLED, value).apply()
        }
}