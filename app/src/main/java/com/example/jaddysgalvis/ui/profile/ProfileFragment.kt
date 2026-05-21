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
import com.example.jaddysgalvis.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProfileBinding.bind(view)

        setupProfile()
        setupButtons()
        setupDarkMode()
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

    // ---------------- PROFILE ----------------

    private fun setupProfile() {

        binding.txtName.text = "Administrador"

        binding.txtEmail.text = "admin@nodo.com"

        val db = AppDatabase.getDatabase(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {

            db.reportDao().getAllReports().collect { reports ->

                val total = reports.size

                val solved = reports.count {
                    it.status == "Resuelto"
                }

                binding.txtReports.text = total.toString()

                binding.txtSolved.text = solved.toString()
            }
        }
    }

    // ---------------- BUTTONS ----------------

    private fun setupButtons() {

        binding.btnLogout.setOnClickListener {

            val prefs = requireActivity()
                .getSharedPreferences("session", Context.MODE_PRIVATE)

            prefs.edit()
                .putBoolean("is_logged", false)
                .apply()

            findNavController().navigate(R.id.loginFragment)
        }
    }

    // ---------------- DESTROY ----------------

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}