package com.lecture_list.extension

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.getDialog(message: String, title: String): AlertDialog {
    val builder = AlertDialog.Builder(this).apply {
        setMessage(message)
        setTitle(title)
    }
    return builder.create()
}