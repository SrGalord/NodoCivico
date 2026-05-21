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
import com.example.jaddysgalvis.data.session.SessionManager
import com.example.jaddysgalvis.databinding.FragmentReportDetailBinding
import kotlinx.coroutines.launch

class ReportDetailFragment : Fragment() {

    private var _binding: FragmentReportDetailBinding? = null
    private val binding get() = _binding!!

    private var reportId: Int = 0
    private var currentUserId: Int = 0

    private val statusList = listOf("Pendiente", "En proceso", "Resuelto")
    private val categoryList = listOf("General", "Seguridad", "Basura", "Alumbrado", "Vías")
    private val priorityList = listOf("Alta", "Media", "Baja")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReportDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setupSpinners()
        getArgumentsData()
        setupListeners()
    }

    // ---------------- SPINNERS ----------------

    private fun setupSpinners() {

        binding.spinnerStatus.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            statusList
        )

        binding.spinnerCategory.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryList
        )

        binding.spinnerPriority.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            priorityList
        )
    }

    // ---------------- ARGUMENTS ----------------

    private fun getArgumentsData() {

        reportId = arguments?.getInt("id") ?: 0

        // 🔥 SESSION REAL
        currentUserId = SessionManager.getUserId(requireContext())

        binding.edtTitle.setText(arguments?.getString("title") ?: "")
        binding.edtDescription.setText(arguments?.getString("description") ?: "")
        binding.edtLocation.setText(arguments?.getString("location") ?: "")
        binding.txtDate.text = arguments?.getString("date") ?: "Sin fecha"

        setSpinnerSelection(statusList, arguments?.getString("status"), binding.spinnerStatus)
        setSpinnerSelection(categoryList, arguments?.getString("category"), binding.spinnerCategory)
        setSpinnerSelection(priorityList, arguments?.getString("priority"), binding.spinnerPriority)
    }

    private fun setSpinnerSelection(
        list: List<String>,
        value: String?,
        spinner: android.widget.Spinner
    ) {
        val pos = list.indexOf(value)
        if (pos >= 0) spinner.setSelection(pos)
    }

    // ---------------- LISTENERS ----------------

    private fun setupListeners() {

        binding.btnSaveChanges.setOnClickListener {
            updateReport()
        }

        binding.btnDelete.setOnClickListener {
            deleteReport()
        }
    }

    // ---------------- UPDATE ----------------

    private fun updateReport() {

        val title = binding.edtTitle.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()
        val location = binding.edtLocation.text.toString().trim()

        if (title.isEmpty()) {
            binding.edtTitle.error = "Ingrese un título"
            return
        }

        if (description.isEmpty()) {
            binding.edtDescription.error = "Ingrese una descripción"
            return
        }

        lifecycleScope.launch {

            val db = AppDatabase.getDatabase(requireContext())

            val updatedReport = ReportEntity(
                id = reportId,
                title = title,
                description = description,
                category = binding.spinnerCategory.selectedItem.toString(),
                priority = binding.spinnerPriority.selectedItem.toString(),
                status = binding.spinnerStatus.selectedItem.toString(),
                location = location,
                date = binding.txtDate.text.toString(),

                // 🔥 CORRECTO TIPO INT
                userId = currentUserId
            )

            db.reportDao().updateReport(updatedReport)

            Toast.makeText(
                requireContext(),
                "Reporte actualizado",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigateUp()
        }
    }

    // ---------------- DELETE ----------------

    private fun deleteReport() {

        lifecycleScope.launch {

            val db = AppDatabase.getDatabase(requireContext())

            val reportToDelete = ReportEntity(
                id = reportId,
                title = binding.edtTitle.text.toString(),
                description = binding.edtDescription.text.toString(),
                category = binding.spinnerCategory.selectedItem.toString(),
                priority = binding.spinnerPriority.selectedItem.toString(),
                status = binding.spinnerStatus.selectedItem.toString(),
                location = binding.edtLocation.text.toString(),
                date = binding.txtDate.text.toString(),

                userId = currentUserId
            )

            db.reportDao().deleteReport(reportToDelete)

            Toast.makeText(
                requireContext(),
                "Reporte eliminado",
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