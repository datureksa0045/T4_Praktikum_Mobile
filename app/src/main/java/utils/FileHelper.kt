package utils

import android.content.Context
import java.io.File

object FileHelper {
    private fun getFileName(nim: String): String = "note_$nim.txt"

    fun saveNote(context: Context, nim: String, content: String) {
        val fileName = getFileName(nim)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
        }
    }

    fun loadNote(context: Context, nim: String): String {
        val file = File(context.filesDir, getFileName(nim))
        return if (file.exists()) {
            context.openFileInput(getFileName(nim)).bufferedReader().use { it.readText() }
        } else ""
    }

    fun getFileSize(context: Context, nim: String): Long {
        val file = File(context.filesDir, getFileName(nim))
        return if (file.exists()) file.length() else 0
    }

    fun isNoteExists(context: Context, nim: String): Boolean {
        return File(context.filesDir, getFileName(nim)).exists()
    }

    fun getAllNotes(context: Context): List<File> {
        return context.filesDir.listFiles { _, name ->
            name.startsWith("note_") && name.endsWith(".txt")
        }?.toList() ?: emptyList()
    }

    fun deleteNote(context: Context, nim: String): Boolean {
        val file = File(context.filesDir, getFileName(nim))
        return file.delete()
    }
}
