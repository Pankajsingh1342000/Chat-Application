package com.chatapplication

import android.app.Application
import com.chatapplication.permission_manager.PermissionsPreferences

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        PermissionsPreferences.initPermissionSharedPreferences(this)
    }
}