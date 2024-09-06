package com.chatapplication.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("chat_app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val IS_AUTHENTICATED = "is_authenticated"
    }

    fun setAuthenticationStatus(isAuthenticated: Boolean){
        val editor = prefs.edit()
        editor.putBoolean(IS_AUTHENTICATED, isAuthenticated)
        editor.apply()
    }

    fun isAuthenticated(): Boolean {
        return prefs.getBoolean(IS_AUTHENTICATED, false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

}