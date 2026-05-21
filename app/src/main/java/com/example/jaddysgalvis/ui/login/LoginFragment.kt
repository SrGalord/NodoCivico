package com.example.jaddysgalvis.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)

        setupLogin()
    }

    private fun setupLogin() {

        binding.btnLogin.setOnClickListener {

            val user = binding.edtUser.text.toString().trim()
            val pass = binding.edtPassword.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                binding.edtUser.error = "Campo requerido"
                binding.edtPassword.error = "Campo requerido"
                return@setOnClickListener
            }

            // LOGIN SIMPLE (FAKE LOGIN)
            if (user == "admin" && pass == "1234") {

                val prefs = requireActivity()
                    .getSharedPreferences("session", Context.MODE_PRIVATE)

                prefs.edit()
                    .putBoolean("is_logged", true)
                    .apply()

                findNavController().navigate(R.id.reportsFragment)

            } else {
                binding.edtPassword.error = "Contraseña incorrecta"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}