package com.example.jaddysgalvis.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.databinding.FragmentAdminDashboardBinding
import kotlinx.coroutines.launch

class AdminDashboardFragment : Fragment(R.layout.fragment_admin_dashboard) {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAdminDashboardBinding.bind(view)

        loadData()
    }

    private fun loadData() {

        val db = AppDatabase.getDatabase(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {

            db.reportDao().getAllReports().collect { reports ->

                binding.txtTotal.text = reports.size.toString()

                binding.txtUsers.text =
                    reports.map { it.userId }.distinct().size.toString()

                binding.txtPendientes.text =
                    reports.count { it.status == "Pendiente" }.toString()

                binding.txtProceso.text =
                    reports.count { it.status == "En proceso" }.toString()

                binding.txtResuelto.text =
                    reports.count { it.status == "Resuelto" }.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}