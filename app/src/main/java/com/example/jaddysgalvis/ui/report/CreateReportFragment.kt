package com.example.jaddysgalvis.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.data.repository.ReportRepository
import com.example.jaddysgalvis.databinding.FragmentCreateReportBinding
import com.example.jaddysgalvis.viewmodel.ReportViewModel
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

        _binding = FragmentCreateReportBinding.inflate(
            inflater,
            container,
            false
        )

        setupViewModel()

        setupDropdowns()

        setupListeners()

        return binding.root
    }

    // ---------------- VIEWMODEL ----------------

    private fun setupViewModel() {

        val database =
            AppDatabase.getDatabase(requireContext())

        val repository =
            ReportRepository(database.reportDao())

        viewModel =
            ReportViewModel(repository)
    }

    // ---------------- DROPDOWNS ----------------

    private fun setupDropdowns() {

        val categories = listOf(
            "General",
            "Basura",
            "Seguridad",
            "Alumbrado",
            "Transporte"
        )

        val priorities = listOf(
            "Alta",
            "Media",
            "Baja"
        )

        val status = listOf(
            "Pendiente",
            "En proceso",
            "Resuelto"
        )

        binding.autoCategory.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                categories
            )
        )

        binding.autoPriority.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                priorities
            )
        )

        binding.autoStatus.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                status
            )
        )

        // VALORES POR DEFECTO

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

        val title =
            binding.edtTitle.text.toString().trim()

        val description =
            binding.edtDescription.text.toString().trim()

        val category =
            binding.autoCategory.text.toString().trim()

        val priority =
            binding.autoPriority.text.toString().trim()

        val status =
            binding.autoStatus.text.toString().trim()

        val location =
            binding.edtLocation.text.toString().trim()

        // ---------------- VALIDACIONES ----------------

        if (title.isEmpty()) {

            binding.edtTitle.error =
                "El título es obligatorio"

            binding.edtTitle.requestFocus()

            return
        }

        if (title.length < 5) {

            binding.edtTitle.error =
                "Mínimo 5 caracteres"

            binding.edtTitle.requestFocus()

            return
        }

        if (description.isEmpty()) {

            binding.edtDescription.error =
                "La descripción es obligatoria"

            binding.edtDescription.requestFocus()

            return
        }

        if (description.length < 10) {

            binding.edtDescription.error =
                "Mínimo 10 caracteres"

            binding.edtDescription.requestFocus()

            return
        }

        if (location.isEmpty()) {

            binding.edtLocation.error =
                "La ubicación es obligatoria"

            binding.edtLocation.requestFocus()

            return
        }

        // ---------------- FECHA ----------------

        val currentDate = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        ).format(Date())

        // ---------------- ENTITY ----------------

        val report = ReportEntity(

            title = title,

            description = description,

            category = category,

            priority = priority,

            status = status,

            location = location,

            date = currentDate
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

    // ---------------- DESTROY ----------------

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}