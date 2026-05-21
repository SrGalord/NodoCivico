package com.example.jaddysgalvis.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.UserEntity
import com.example.jaddysgalvis.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRegisterBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {

        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        // 🔥 VALIDACIONES
        if (name.isEmpty()) {
            binding.edtName.error = "Ingrese su nombre"
            binding.edtName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.edtEmail.error = "Ingrese su email"
            binding.edtEmail.requestFocus()
            return
        }

        if (password.length < 4) {
            binding.edtPassword.error = "Mínimo 4 caracteres"
            binding.edtPassword.requestFocus()
            return
        }

        // 🔥 POR DEFECTO TODOS SON USER
        val user = UserEntity(
            name = name,
            email = email,
            password = password,
            role = "USER"
        )

        lifecycleScope.launch {

            val db = AppDatabase.getDatabase(requireContext())

            db.userDao().registerUser(user)

            Toast.makeText(
                requireContext(),
                "Usuario creado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}