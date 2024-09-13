package com.chatapplication.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Util {

    object KeyboardHelper {
        fun hideKeyboard(activity: Activity) {
            // Get the current focused view
            val view: View? = activity.currentFocus
            view?.let { v ->
                // Get InputMethodManager
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                // Hide the keyboard
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }
}
