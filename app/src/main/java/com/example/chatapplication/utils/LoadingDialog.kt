package com.example.chatapplication.utils

import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView
import com.example.chatapplication.R

class LoadingDialog(val mActivity : Activity, val inputmessage : String){


    private lateinit var isdialog : AlertDialog

    fun startLoading(){

        val inflator = mActivity.layoutInflater
        val dialogView = inflator.inflate(R.layout.loading_dialog, null)

        val message : TextView = dialogView.findViewById(R.id.message)
        message.text = inputmessage

        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()

    }

    fun dismiss(){
        isdialog.dismiss()
    }

}