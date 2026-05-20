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
import com.example.jaddysgalvis.databinding.FragmentReportDetailBinding
import kotlinx.coroutines.launch

class ReportDetailFragment : Fragment() {

    private var _binding: FragmentReportDetailBinding? = null
    private val binding get() = _binding!!

    private var reportId: Int = 0

    private val statusList = listOf(
        "Pendiente",
        "En proceso",
        "Resuelto"
    )

    private val categoryList = listOf(
        "General",
        "Seguridad",
        "Basura",
        "Alumbrado",
        "Vías"
    )

    private val priorityList = listOf(
        "Alta",
        "Media",
        "Baja"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReportDetailBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        setupSpinners()

        getArgumentsData()

        setupListeners()
    }

    private fun setupSpinners() {

        // STATUS

        val statusAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            statusList
        )

        binding.spinnerStatus.adapter = statusAdapter

        // CATEGORY

        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryList
        )

        binding.spinnerCategory.adapter = categoryAdapter

        // PRIORITY

        val priorityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            priorityList
        )

        binding.spinnerPriority.adapter = priorityAdapter
    }

    private fun getArgumentsData() {

        reportId =
            arguments?.getInt("id") ?: 0

        binding.edtTitle.setText(
            arguments?.getString("title")
        )

        binding.edtDescription.setText(
            arguments?.getString("description")
        )

        binding.edtLocation.setText(
            arguments?.getString("location")
        )

        binding.txtDate.text =
            arguments?.getString("date")

        // STATUS

        val currentStatus =
            arguments?.getString("status")

        val statusPosition =
            statusList.indexOf(currentStatus)

        if (statusPosition >= 0) {

            binding.spinnerStatus.setSelection(
                statusPosition
            )
        }

        // CATEGORY

        val currentCategory =
            arguments?.getString("category")

        val categoryPosition =
            categoryList.indexOf(currentCategory)

        if (categoryPosition >= 0) {

            binding.spinnerCategory.setSelection(
                categoryPosition
            )
        }

        // PRIORITY

        val currentPriority =
            arguments?.getString("priority")

        val priorityPosition =
            priorityList.indexOf(currentPriority)

        if (priorityPosition >= 0) {

            binding.spinnerPriority.setSelection(
                priorityPosition
            )
        }
    }

    private fun setupListeners() {

        binding.btnSaveChanges.setOnClickListener {

            updateReport()
        }

        binding.btnDelete.setOnClickListener {

            deleteReport()
        }
    }

    private fun updateReport() {

        val title =
            binding.edtTitle.text.toString().trim()

        val description =
            binding.edtDescription.text.toString().trim()

        val location =
            binding.edtLocation.text.toString().trim()

        // VALIDACIONES

        if (title.isEmpty()) {

            binding.edtTitle.error =
                "Ingrese un título"

            binding.edtTitle.requestFocus()

            return
        }

        if (description.isEmpty()) {

            binding.edtDescription.error =
                "Ingrese una descripción"

            binding.edtDescription.requestFocus()

            return
        }

        lifecycleScope.launch {

            try {

                val db =
                    AppDatabase.getDatabase(
                        requireContext()
                    )

                val updatedReport = ReportEntity(

                    id = reportId,

                    title = title,

                    description = description,

                    category =
                        binding.spinnerCategory
                            .selectedItem.toString(),

                    priority =
                        binding.spinnerPriority
                            .selectedItem.toString(),

                    status =
                        binding.spinnerStatus
                            .selectedItem.toString(),

                    location = location,

                    date =
                        binding.txtDate.text.toString()
                )

                db.reportDao()
                    .updateReport(updatedReport)

                Toast.makeText(
                    requireContext(),
                    "Reporte actualizado",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigateUp()

            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Error actualizando reporte",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun deleteReport() {

        lifecycleScope.launch {

            try {

                val db =
                    AppDatabase.getDatabase(
                        requireContext()
                    )

                val report = ReportEntity(

                    id = reportId,

                    title =
                        binding.edtTitle.text.toString(),

                    description =
                        binding.edtDescription.text.toString(),

                    category =
                        binding.spinnerCategory
                            .selectedItem.toString(),

                    priority =
                        binding.spinnerPriority
                            .selectedItem.toString(),

                    status =
                        binding.spinnerStatus
                            .selectedItem.toString(),

                    location =
                        binding.edtLocation.text.toString(),

                    date =
                        binding.txtDate.text.toString()
                )

                db.reportDao()
                    .deleteReport(report)

                Toast.makeText(
                    requireContext(),
                    "Reporte eliminado",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigateUp()

            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Error eliminando reporte",
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