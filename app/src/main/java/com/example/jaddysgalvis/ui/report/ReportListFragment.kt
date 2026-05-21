package com.example.jaddysgalvis.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.databinding.FragmentReportListBinding
import com.example.jaddysgalvis.ui.report.adapter.ReportAdapter
import kotlinx.coroutines.launch

class ReportListFragment : Fragment() {

    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!

    private var reportList: List<ReportEntity> = emptyList()

    private var adapter: ReportAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentReportListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupAdapter()
        setupSearch()
        setupButtons()
        observeReports()
    }

    // ---------------- BUTTONS ----------------

    private fun setupButtons() {

        binding.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.fabAddReport.setOnClickListener {

            findNavController().navigate(
                R.id.createReportFragment
            )
        }
    }

    // ---------------- RECYCLER ----------------

    private fun setupRecycler() {

        binding.recyclerReports.layoutManager =
            LinearLayoutManager(requireContext())
    }

    // ---------------- ADAPTER ----------------

    private fun setupAdapter() {

        adapter = ReportAdapter { report ->

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
        }

        binding.recyclerReports.adapter = adapter
    }

    // ---------------- SEARCH ----------------

    private fun setupSearch() {

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {

                    filterReports(query.orEmpty())

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    filterReports(newText.orEmpty())

                    return true
                }
            }
        )
    }

    private fun filterReports(text: String) {

        val filtered = reportList.filter {

            it.title.contains(text, true) ||
                    it.description.contains(text, true)
        }

        adapter?.updateData(filtered)

        updateEmptyState(filtered)
        updateDashboard(filtered)
    }

    // ---------------- FLOW ROOM ----------------

    private fun observeReports() {

        viewLifecycleOwner.lifecycleScope.launch {

            val db = AppDatabase.getDatabase(requireContext())

            db.reportDao().getAllReports().collect { reports ->

                if (_binding == null) return@collect

                reportList = reports

                adapter?.updateData(reports)

                updateEmptyState(reports)
                updateDashboard(reports)
            }
        }
    }

    // ---------------- EMPTY STATE ----------------

    private fun updateEmptyState(
        reports: List<ReportEntity>
    ) {

        if (_binding == null) return

        if (reports.isEmpty()) {

            binding.recyclerReports.visibility = View.GONE
            binding.emptyContainer.visibility = View.VISIBLE

        } else {

            binding.recyclerReports.visibility = View.VISIBLE
            binding.emptyContainer.visibility = View.GONE
        }
    }

    // ---------------- DASHBOARD ----------------

    private fun updateDashboard(
        reports: List<ReportEntity>
    ) {

        if (_binding == null) return

        val total = reports.size

        val pendiente =
            reports.count { it.status == "Pendiente" }

        val proceso =
            reports.count { it.status == "En proceso" }

        val resuelto =
            reports.count { it.status == "Resuelto" }

        binding.txtTotal.text = "Total\n$total"
        binding.txtPendiente.text = "Pend\n$pendiente"
        binding.txtProceso.text = "Proc\n$proceso"
        binding.txtResuelto.text = "OK\n$resuelto"
    }

    // ---------------- DESTROY ----------------

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}