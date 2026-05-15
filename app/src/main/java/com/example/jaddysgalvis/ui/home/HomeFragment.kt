package com.example.jaddysgalvis.ui.home

import android.content.IntentFilter
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.jaddysgalvis.data.remote.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.receiver.ConnectivityReceiver

class HomeFragment : Fragment() {

    private lateinit var connectivityReceiver: ConnectivityReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )

        val btnReports =
            view.findViewById<Button>(R.id.btnReports)

        val btnCreate =
            view.findViewById<Button>(R.id.btnCreate)

        val txtConnectionStatus =
            view.findViewById<TextView>(
                R.id.txtConnectionStatus
            )

        // BOTÓN REPORTES

        btnReports.setOnClickListener {

            findNavController().navigate(
                R.id.reportsFragment
            )
        }

        // BOTÓN CREAR

        btnCreate.setOnClickListener {

            findNavController().navigate(
                R.id.createReportFragment
            )
        }

        // RECEIVER

        connectivityReceiver = ConnectivityReceiver()

        // VERIFICAR ESTADO INICIAL

        val connectivityManager =
            requireContext().getSystemService(
                ConnectivityManager::class.java
            )

        val activeNetwork =
            connectivityManager.activeNetworkInfo

        val isConnected =
            activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting

        if (isConnected) {

            txtConnectionStatus.text = "🟢 Online"

            txtConnectionStatus.setTextColor(
                Color.GREEN
            )

        } else {

            txtConnectionStatus.text = "🔴 Sin conexión"

            txtConnectionStatus.setTextColor(
                Color.RED
            )
        }

        // REGISTRAR RECEIVER

        requireContext().registerReceiver(
            connectivityReceiver,
            IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION
            )
        )

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient.api.getReports()

                if (response.isSuccessful) {

                    Log.d(
                        "API_TEST",
                        response.body().toString()
                    )

                } else {

                    Log.d(
                        "API_TEST",
                        "Error API"
                    )
                }

            } catch (e: Exception) {

                Log.d(
                    "API_TEST",
                    e.message.toString()
                )
            }
        }

        return view
    }

    override fun onDestroy() {

        super.onDestroy()

        requireContext().unregisterReceiver(
            connectivityReceiver
        )
    }
}