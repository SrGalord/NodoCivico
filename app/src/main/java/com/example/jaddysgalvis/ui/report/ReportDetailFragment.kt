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
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.databinding.FragmentReportDetailBinding
import kotlinx.coroutines.launch

class ReportDetailFragment : Fragment() {

    private var _binding: FragmentReportDetailBinding? = null
    private val binding get() = _binding!!

    private var reportId: Int = 0

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

        reportId = arguments?.getInt("id") ?: 0

        binding.txtTitle.text =
            arguments?.getString("title")

        binding.txtDescription.text =
            arguments?.getString("description")

        binding.txtStatus.text =
            arguments?.getString("status")

        binding.txtCategory.text =
            arguments?.getString("category")

        binding.txtPriority.text =
            arguments?.getString("priority")

        binding.txtLocation.text =
            arguments?.getString("location")

        binding.txtDate.text =
            arguments?.getString("date")

        setupSpinner()

        binding.btnUpdateStatus.setOnClickListener {

            updateStatus()
        }

        binding.btnEdit.setOnClickListener {

            val bundle = Bundle().apply {

                putInt("id", reportId)
                putString("title", binding.txtTitle.text.toString())
                putString("description", binding.txtDescription.text.toString())
            }

            findNavController().navigate(
                R.id.editReportFragment,
                bundle
            )
        }

        binding.btnDelete.setOnClickListener {

            deleteReport()
        }
    }

    private fun setupSpinner() {

        val statusList = listOf(
            "Pendiente",
            "En proceso",
            "Resuelto"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            statusList
        )

        binding.spinnerStatus.adapter = adapter
    }

    private fun updateStatus() {

        lifecycleScope.launch {

            val db = AppDatabase.getDatabase(requireContext())

            val updatedReport = ReportEntity(
                id = reportId,
                title = binding.txtTitle.text.toString(),
                description = binding.txtDescription.text.toString(),
                category = binding.txtCategory.text.toString(),
                priority = binding.txtPriority.text.toString(),
                status = binding.spinnerStatus.selectedItem.toString(),
                location = binding.txtLocation.text.toString(),
                date = binding.txtDate.text.toString()
            )

            db.reportDao().updateReport(updatedReport)

            binding.txtStatus.text =
                binding.spinnerStatus.selectedItem.toString()

            Toast.makeText(
                requireContext(),
                "Estado actualizado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun deleteReport() {

        lifecycleScope.launch {

            val db = AppDatabase.getDatabase(requireContext())

            val report = ReportEntity(
                id = reportId,
                title = binding.txtTitle.text.toString(),
                description = binding.txtDescription.text.toString(),
                category = binding.txtCategory.text.toString(),
                priority = binding.txtPriority.text.toString(),
                status = binding.txtStatus.text.toString(),
                location = binding.txtLocation.text.toString(),
                date = binding.txtDate.text.toString()
            )

            db.reportDao().deleteReport(report)

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