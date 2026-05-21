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
import com.example.jaddysgalvis.data.session.SessionManager
import com.example.jaddysgalvis.databinding.FragmentEditReportBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditReportFragment : Fragment() {

    private var _binding: FragmentEditReportBinding? = null
    private val binding get() = _binding!!

    private var reportId: Int = 0
    private var currentUserId: Int = 0

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

        // 🔥 USER REAL DE SESIÓN
        currentUserId = SessionManager.getUserId(requireContext())

        binding.btnUpdate.setOnClickListener {
            updateReport()
        }
    }

    private fun updateReport() {

        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (title.isEmpty()) {
            binding.etTitle.error = "El título es obligatorio"
            return
        }

        if (description.isEmpty()) {
            binding.etDescription.error = "La descripción es obligatoria"
            return
        }

        lifecycleScope.launch {

            val db = AppDatabase.getDatabase(requireContext())

            val currentDate = SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.getDefault()
            ).format(Date())

            val updatedReport = ReportEntity(
                id = reportId,
                title = title,
                description = description,

                category = arguments?.getString("category") ?: "General",
                priority = arguments?.getString("priority") ?: "Media",
                status = arguments?.getString("status") ?: "Pendiente",
                location = arguments?.getString("location") ?: "Sin ubicación",

                date = currentDate,

                // 🔥 YA ES INT (CORRECTO)
                userId = currentUserId
            )

            db.reportDao().updateReport(updatedReport)

            Toast.makeText(
                requireContext(),
                "Reporte actualizado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}