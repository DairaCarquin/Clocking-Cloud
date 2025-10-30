package com.clockingcloud.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("clocking_prefs", Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getAccessToken(): String? = prefs.getString("access_token", null)

    fun saveRefreshToken(token: String) {
        prefs.edit().putString("refresh_token", token).apply()
    }

    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun saveUserId(id: Long) {
        prefs.edit().putLong("user_id", id).apply()
    }

    fun getUserId(): Long = prefs.getLong("user_id", -1)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}