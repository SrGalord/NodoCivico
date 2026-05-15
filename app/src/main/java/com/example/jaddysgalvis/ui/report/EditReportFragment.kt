package com.example.jaddysgalvis.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.databinding.FragmentEditReportBinding
import kotlinx.coroutines.launch

class EditReportFragment : Fragment() {

    private var _binding: FragmentEditReportBinding? = null
    private val binding get() = _binding!!

    private var reportId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditReportBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportId = arguments?.getInt("id") ?: 0

        binding.etTitle.setText(arguments?.getString("title"))
        binding.etDescription.setText(arguments?.getString("description"))

        binding.btnUpdate.setOnClickListener {

            updateReport()
        }
    }

    private fun updateReport() {

        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        // VALIDACIONES

        if (title.isEmpty()) {
            binding.etTitle.error = "El título es obligatorio"
            binding.etTitle.requestFocus()
            return
        }

        if (title.length < 5) {
            binding.etTitle.error = "Mínimo 5 caracteres"
            binding.etTitle.requestFocus()
            return
        }

        if (description.isEmpty()) {
            binding.etDescription.error = "La descripción es obligatoria"
            binding.etDescription.requestFocus()
            return
        }

        if (description.length < 10) {
            binding.etDescription.error =
                "La descripción debe tener mínimo 10 caracteres"
            binding.etDescription.requestFocus()
            return
        }

        lifecycleScope.launch {

            try {

                val db = AppDatabase.getDatabase(requireContext())

                val updatedReport = ReportEntity(
                    id = reportId,
                    title = title,
                    description = description,
                    category = "General",
                    priority = "Media",
                    status = "Pendiente",
                    location = "Sin ubicación",
                    date = "14/05/2026"
                )

                db.reportDao().updateReport(updatedReport)

                Toast.makeText(
                    requireContext(),
                    "Reporte actualizado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                parentFragmentManager.popBackStack()

            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Error al actualizar el reporte",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}