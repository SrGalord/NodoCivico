package com.example.jaddysgalvis.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        Toast.makeText(
            context,
            "Tienes acciones pendientes por revisar",
            Toast.LENGTH_LONG
        ).show()
    }
}