package com.example.jaddysgalvis.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.session.SessionManager
import com.example.jaddysgalvis.databinding.FragmentUserDashboardBinding
import kotlinx.coroutines.launch

class UserDashboardFragment : Fragment(R.layout.fragment_user_dashboard) {

    private var _binding: FragmentUserDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUserDashboardBinding.bind(view)

        loadData()
    }

    private fun loadData() {

        val userId = SessionManager.getUserId(requireContext())

        val db = AppDatabase.getDatabase(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {

            db.reportDao().getAllReports().collect { reports ->

                val myReports = reports.filter { it.userId == userId }

                binding.txtTotal.text = myReports.size.toString()

                binding.txtPendientes.text =
                    myReports.count { it.status == "Pendiente" }.toString()

                binding.txtProceso.text =
                    myReports.count { it.status == "En proceso" }.toString()

                binding.txtResuelto.text =
                    myReports.count { it.status == "Resuelto" }.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}