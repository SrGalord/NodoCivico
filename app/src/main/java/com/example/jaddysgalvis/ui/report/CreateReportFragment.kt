package com.example.jaddysgalvis.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        setupListeners()

        return binding.root
    }

    private fun setupViewModel() {

        val database =
            AppDatabase.getDatabase(requireContext())

        val repository =
            ReportRepository(database.reportDao())

        viewModel =
            ReportViewModel(repository)
    }

    private fun setupListeners() {

        binding.btnSave.setOnClickListener {

            createReport()
        }
    }

    private fun createReport() {

        val title =
            binding.edtTitle.text.toString().trim()

        val description =
            binding.edtDescription.text.toString().trim()

        // VALIDACIONES

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

        val currentDate = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        ).format(Date())

        val report = ReportEntity(

            title = title,

            description = description,

            category = "General",

            priority = "Alta",

            status = "Pendiente",

            location = "Sin ubicación",

            date = currentDate
        )

        viewModel.insertReport(report)

        Toast.makeText(
            requireContext(),
            "Reporte creado correctamente",
            Toast.LENGTH_SHORT
        ).show()

        clearFields()

        findNavController().navigateUp()
    }

    private fun clearFields() {

        binding.edtTitle.setText("")

        binding.edtDescription.setText("")
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}