package com.example.jaddysgalvis.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.session.SessionManager
import com.example.jaddysgalvis.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)

        setupLogin()
        setupRegister()
    }

    private fun setupLogin() {

        binding.btnLogin.setOnClickListener {

            // 🔥 ahora puede ser email o nombre
            val input = binding.edtUser.text.toString().trim()
            val pass = binding.edtPassword.text.toString().trim()

            if (input.isEmpty() || pass.isEmpty()) {
                binding.edtUser.error = "Campo requerido"
                binding.edtPassword.error = "Campo requerido"
                return@setOnClickListener
            }

            val db = AppDatabase.getDatabase(requireContext())

            lifecycleScope.launch {

                val user = db.userDao().loginUser(input, pass)

                if (user != null) {

                    SessionManager.saveUser(
                        requireContext(),
                        user.id,
                        user.role
                    )

                    println("LOGIN OK -> ${user.name} | ROLE: ${user.role}")

                    findNavController().navigate(R.id.reportsFragment)

                } else {
                    binding.edtPassword.error = "Credenciales incorrectas"
                }
            }
        }
    }

    private fun setupRegister() {

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}