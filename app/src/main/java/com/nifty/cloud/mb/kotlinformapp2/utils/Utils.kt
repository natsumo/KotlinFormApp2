package com.nifty.cloud.mb.kotlinformapp2.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object Utils {
    fun formatTime(time: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val output = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        try {
            val d = sdf.parse(time)
            return output.format(d)
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }

    }

    fun showDialog(context: Context, message: String) {
        val mbuilder = AlertDialog.Builder(context)
        mbuilder.setTitle("Alert")
        mbuilder.setMessage(message)
        mbuilder.setNegativeButton("OK") { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog = mbuilder.create()
        alertDialog.show()
    }
}
