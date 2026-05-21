package com.example.jaddysgalvis.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.data.repository.ReportRepository
import com.example.jaddysgalvis.data.session.SessionManager
import com.example.jaddysgalvis.databinding.FragmentCreateReportBinding
import com.example.jaddysgalvis.viewmodel.ReportViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateReportFragment : Fragment() {

    private var _binding: FragmentCreateReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateReportBinding.inflate(inflater, container, false)

        setupViewModel()
        setupDropdowns()
        setupListeners()

        return binding.root
    }

    // ---------------- VIEWMODEL ----------------

    private fun setupViewModel() {

        val database = AppDatabase.getDatabase(requireContext())
        val repository = ReportRepository(database.reportDao())

        viewModel = ReportViewModel(repository)
    }

    // ---------------- DROPDOWNS ----------------

    private fun setupDropdowns() {

        val categories = listOf("General", "Basura", "Seguridad", "Alumbrado", "Transporte")
        val priorities = listOf("Alta", "Media", "Baja")
        val status = listOf("Pendiente", "En proceso", "Resuelto")

        binding.autoCategory.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        )

        binding.autoPriority.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, priorities)
        )

        binding.autoStatus.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, status)
        )

        binding.autoCategory.setText("General", false)
        binding.autoPriority.setText("Media", false)
        binding.autoStatus.setText("Pendiente", false)
    }

    // ---------------- LISTENERS ----------------

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            createReport()
        }
    }

    // ---------------- CREATE REPORT ----------------

    private fun createReport() {

        val title = binding.edtTitle.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()
        val category = binding.autoCategory.text.toString().trim()
        val priority = binding.autoPriority.text.toString().trim()
        val location = binding.edtLocation.text.toString().trim()

        // ---------------- VALIDACIONES ----------------

        if (title.isEmpty()) {
            binding.edtTitle.error = "El título es obligatorio"
            return
        }

        if (description.isEmpty()) {
            binding.edtDescription.error = "La descripción es obligatoria"
            return
        }

        if (location.isEmpty()) {
            binding.edtLocation.error = "La ubicación es obligatoria"
            return
        }

        // ---------------- DATE ----------------

        val currentDate = SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            Locale.getDefault()
        ).format(Date())

        // ---------------- USER SESSION (CORRECTO) ----------------

        val userId = SessionManager.getUserId(requireContext())

        if (userId == -1) {
            Toast.makeText(requireContext(), "Sesión inválida", Toast.LENGTH_SHORT).show()
            return
        }

        // ---------------- ENTITY ----------------

        val report = ReportEntity(
            title = title,
            description = description,
            category = category,
            priority = priority,
            status = "Pendiente",
            location = location,
            date = currentDate,
            userId = userId
        )

        // ---------------- SAVE ----------------

        viewModel.insertReport(report)

        Toast.makeText(
            requireContext(),
            "Reporte creado correctamente",
            Toast.LENGTH_SHORT
        ).show()

        clearFields()

        findNavController().navigateUp()
    }

    // ---------------- CLEAR ----------------

    private fun clearFields() {

        binding.edtTitle.setText("")
        binding.edtDescription.setText("")
        binding.edtLocation.setText("")

        binding.autoCategory.setText("General", false)
        binding.autoPriority.setText("Media", false)
        binding.autoStatus.setText("Pendiente", false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}