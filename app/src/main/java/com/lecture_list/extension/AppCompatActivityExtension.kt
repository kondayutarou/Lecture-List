package com.lecture_list.extension

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lecture_list.R

fun AppCompatActivity.getDialog(
    message: String,
    title: String,
    listener: DialogInterface.OnClickListener
): AlertDialog {
    val builder = AlertDialog.Builder(this).apply {
        setMessage(message)
        setTitle(title)
        setPositiveButton(this@getDialog.getString(R.string.dialog_ok), listener)
    }
    return builder.create()
}