package com.chatapplication.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("chat_app_prefs", Context.MODE_PRIVATE)

    fun setUserLoggedIn(isLoggedin: Boolean){
        prefs.edit().putBoolean("is_logged_in", isLoggedin).apply()
    }

    fun isUserLoggedIn(): Boolean{
        return prefs.getBoolean("is_logged_in", false)
    }

}