package com.monsalud.asteroidalert.presentation

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun displayExplanationDialog(context: Context, message: String) {
    val builder = AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)
    builder.create().show()
}
