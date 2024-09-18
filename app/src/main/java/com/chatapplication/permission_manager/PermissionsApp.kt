package com.chatapplication.permission_manager

import android.app.Application

class PermissionsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PermissionsPreferences.initPermissionSharedPreferences(this)
    }
}