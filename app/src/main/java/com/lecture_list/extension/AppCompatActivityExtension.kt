package com.lecture_list.extension

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lecture_list.R
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

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

fun AppCompatActivity.isInternetAvailable(): Boolean {
    var result = false
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }

    return result
}