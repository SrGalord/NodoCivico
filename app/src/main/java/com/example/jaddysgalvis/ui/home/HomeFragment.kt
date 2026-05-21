package com.example.jaddysgalvis.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        loadStats()
        setupButtons()
    }

    private fun setupButtons() {

        binding.btnViewReports.setOnClickListener {
            findNavController().navigate(R.id.reportsFragment)
        }

        binding.btnCreate.setOnClickListener {
            findNavController().navigate(R.id.createReportFragment)
        }

        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun loadStats() {

        val db = AppDatabase.getDatabase(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {

            db.reportDao().getAllReports().collect { reports ->

                val total = reports.size
                val pending = reports.count { it.status == "Pendiente" }
                val process = reports.count { it.status == "En proceso" }
                val solved = reports.count { it.status == "Resuelto" }

                binding.txtTotal.text = total.toString()
                binding.txtPending.text = pending.toString()
                binding.txtProcess.text = process.toString()
                binding.txtSolved.text = solved.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}