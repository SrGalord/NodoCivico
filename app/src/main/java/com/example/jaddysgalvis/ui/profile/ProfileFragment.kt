package com.example.jaddysgalvis.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.session.SessionManager
import com.example.jaddysgalvis.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        _binding = FragmentProfileBinding.bind(view)

        setupProfile()
        setupButtons()
        setupDarkMode()
    }

    // ---------------- PROFILE ----------------

    private fun setupProfile() {

        val db = AppDatabase.getDatabase(requireContext())

        val role = SessionManager.getUserRole(requireContext())
        val userId = SessionManager.getUserId(requireContext())

        binding.txtName.text = if (role == "ADMIN") "Administrador" else "Usuario"
        binding.txtEmail.text = if (role == "ADMIN") "admin@nodo.com" else "usuario@nodo.com"

        viewLifecycleOwner.lifecycleScope.launch {

            db.reportDao().getAllReports().collect { reports ->

                val filteredReports = if (role == "ADMIN") {
                    reports
                } else {
                    reports.filter { it.userId == userId }
                }

                val total = filteredReports.size
                val solved = filteredReports.count { it.status == "Resuelto" }

                binding.txtReports.text = total.toString()
                binding.txtSolved.text = solved.toString()
            }
        }
    }

    // ---------------- DARK MODE ----------------

    private fun setupDarkMode() {

        val prefs = requireActivity()
            .getSharedPreferences("settings", Context.MODE_PRIVATE)

        val isDark = prefs.getBoolean("dark_mode", false)

        binding.switchDarkMode.setOnCheckedChangeListener(null)
        binding.switchDarkMode.isChecked = isDark

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->

            prefs.edit()
                .putBoolean("dark_mode", isChecked)
                .apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    // ---------------- BUTTONS ----------------

    private fun setupButtons() {

        binding.btnLogout.setOnClickListener {

            SessionManager.clear(requireContext())

            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}