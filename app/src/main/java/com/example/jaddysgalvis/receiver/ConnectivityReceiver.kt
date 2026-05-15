package com.example.jaddysgalvis.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val connectivityManager =
            context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        val activeNetwork =
            connectivityManager.activeNetworkInfo

        val isConnected =
            activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting

        if (isConnected) {

            Toast.makeText(
                context,
                "Conexión a internet disponible",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            Toast.makeText(
                context,
                "Sin conexión a internet",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}