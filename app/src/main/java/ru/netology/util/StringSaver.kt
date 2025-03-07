package ru.netology.util
import android.content.Context

class StringSaver(context: Context, private val fileName: String = "stringStorage") {
    private val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    fun get(key: String = fileName): String {
        return prefs.getString(key, "")!!
    }
    fun put(text: String, key: String = fileName) {
        prefs.edit().putString(key, text).apply()
    }
    fun del(key: String = fileName) {
        prefs.edit().remove(key).apply()
    }
}