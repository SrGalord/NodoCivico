package com.example.jaddysgalvis.data.session

import android.content.Context

object SessionManager {

    private const val PREF = "session"
    private const val KEY_ID = "user_id"
    private const val KEY_ROLE = "user_role"

    fun saveUser(context: Context, userId: Int, role: String) {

        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

        prefs.edit()
            .putInt(KEY_ID, userId)
            .putString(KEY_ROLE, role)
            .apply()
    }

    fun getUserId(context: Context): Int {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getInt(KEY_ID, -1)
    }

    fun getUserRole(context: Context): String {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getString(KEY_ROLE, "USER") ?: "USER"
    }

    fun isAdmin(context: Context): Boolean {
        return getUserRole(context) == "ADMIN"
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}