package com.example.jaddysgalvis.ui.report

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.databinding.FragmentReportListBinding

// IMPORT CORRECTO
import com.example.jaddysgalvis.ui.report.adapter.ReportAdapter

import kotlinx.coroutines.launch

class ReportListFragment : Fragment() {

    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReportListBinding.inflate(
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

        binding.recyclerReports.layoutManager =
            LinearLayoutManager(requireContext())

        loadReports()
    }

    override fun onResume() {

        super.onResume()

        loadReports()
    }

    private fun loadReports() {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {

            try {

                val db = AppDatabase.getDatabase(requireContext())

                val reports = db.reportDao().getAllReports()

                binding.progressBar.visibility = View.GONE

                if (reports.isEmpty()) {

                    binding.recyclerReports.visibility = View.GONE
                    binding.txtEmpty.visibility = View.VISIBLE

                } else {

                    binding.recyclerReports.visibility = View.VISIBLE
                    binding.txtEmpty.visibility = View.GONE
                }

                adapter = ReportAdapter(

                    reports = reports,

                    // DETALLE
                    onDetailClick = { report ->

                        val bundle = Bundle().apply {

                            putInt("id", report.id)
                            putString("title", report.title)
                            putString("description", report.description)
                            putString("status", report.status)
                            putString("category", report.category)
                            putString("priority", report.priority)
                            putString("location", report.location)
                            putString("date", report.date)
                        }

                        findNavController().navigate(
                            R.id.reportDetailFragment,
                            bundle
                        )
                    },

                    // EDITAR
                    onEditClick = { report ->

                        val bundle = Bundle().apply {

                            putInt("id", report.id)
                            putString("title", report.title)
                            putString("description", report.description)
                        }

                        findNavController().navigate(
                            R.id.editReportFragment,
                            bundle
                        )
                    },

                    // ELIMINAR
                    onDeleteClick = { report ->

                        showDeleteDialog(report)
                    }
                )

                binding.recyclerReports.adapter = adapter

            } catch (e: Exception) {

                binding.progressBar.visibility = View.GONE

                Toast.makeText(
                    requireContext(),
                    "Error cargando reportes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showDeleteDialog(report: ReportEntity) {

        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar reporte")
            .setMessage("¿Deseas eliminar este reporte?")
            .setPositiveButton("Sí") { _, _ ->

                deleteReport(report)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteReport(report: ReportEntity) {

        lifecycleScope.launch {

            try {

                val db = AppDatabase.getDatabase(requireContext())

                db.reportDao().deleteReport(report)

                Toast.makeText(
                    requireContext(),
                    "Reporte eliminado",
                    Toast.LENGTH_SHORT
                ).show()

                loadReports()

            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Error al eliminar",
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