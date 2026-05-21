package com.example.jaddysgalvis.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.data.session.SessionManager
import com.example.jaddysgalvis.databinding.FragmentReportListBinding
import com.example.jaddysgalvis.ui.report.adapter.ReportAdapter
import kotlinx.coroutines.launch

class ReportListFragment : Fragment() {

    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!

    private var reportList: List<ReportEntity> = emptyList()
    private var adapter: ReportAdapter? = null

    private var currentFilter: String = "ALL"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupAdapter()
        setupSearch()
        setupButtons()
        setupToggleButtons()
        setupSwipeToDelete()
        observeReports()
    }

    // ---------------- SESSION ----------------

    private fun getUserId(): Int {
        return SessionManager.getUserId(requireContext())
    }

    private fun getUserRole(): String {
        return SessionManager.getUserRole(requireContext())
    }

    // ---------------- BUTTONS ----------------

    private fun setupButtons() {

        binding.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.fabAddReport.setOnClickListener {
            findNavController().navigate(R.id.createReportFragment)
        }
    }

    // ---------------- TOGGLE ----------------

    private fun setupToggleButtons() {

        binding.toggleReports.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            currentFilter = when (checkedId) {
                R.id.btnMyReports -> "MY"
                R.id.btnCommunityReports -> "COMMUNITY"
                else -> "ALL"
            }

            applyFilters()
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

                override fun onQueryTextSubmit(query: String?) = true.also {
                    applyFilters(query.orEmpty())
                }

                override fun onQueryTextChange(newText: String?) = true.also {
                    applyFilters(newText.orEmpty())
                }
            }
        )
    }

    // ---------------- FILTER CORE ----------------

    private fun applyFilters(searchText: String = "") {

        val role = getUserRole()
        val userId = getUserId()

        var list = reportList

        // 🔥 ROLE FILTER
        list = if (role == "USER") {
            list.filter { it.userId == userId }
        } else {
            list
        }

        // 🔥 TAB FILTER
        list = when (currentFilter) {

            "MY" -> list.filter { it.userId == userId }

            "COMMUNITY" -> if (role == "ADMIN") list else list.filter { it.userId == userId }

            else -> list
        }

        // 🔍 SEARCH
        if (searchText.isNotEmpty()) {
            list = list.filter {
                it.title.contains(searchText, true) ||
                        it.description.contains(searchText, true)
            }
        }

        adapter?.updateData(list)
        updateEmptyState(list)
        updateDashboard(list)
    }

    // ---------------- ROOM ----------------

    private fun observeReports() {

        viewLifecycleOwner.lifecycleScope.launch {

            val db = AppDatabase.getDatabase(requireContext())

            db.reportDao().getAllReports().collect { reports ->

                if (_binding == null) return@collect

                reportList = reports
                applyFilters()
            }
        }
    }

    // ---------------- SWIPE DELETE ----------------

    private fun setupSwipeToDelete() {

        if (getUserRole() != "ADMIN") return

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {

                val report = reportList[viewHolder.bindingAdapterPosition]

                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    db.reportDao().deleteReport(report)
                }
            }
        }

        ItemTouchHelper(swipeCallback)
            .attachToRecyclerView(binding.recyclerReports)
    }

    // ---------------- EMPTY ----------------

    private fun updateEmptyState(reports: List<ReportEntity>) {

        if (reports.isEmpty()) {
            binding.recyclerReports.visibility = View.GONE
            binding.emptyContainer.visibility = View.VISIBLE
        } else {
            binding.recyclerReports.visibility = View.VISIBLE
            binding.emptyContainer.visibility = View.GONE
        }
    }

    // ---------------- DASHBOARD ----------------

    private fun updateDashboard(reports: List<ReportEntity>) {

        binding.txtTotal.text = "Total\n${reports.size}"
        binding.txtPendiente.text = "Pend\n${reports.count { it.status == "Pendiente" }}"
        binding.txtProceso.text = "Proc\n${reports.count { it.status == "En proceso" }}"
        binding.txtResuelto.text = "OK\n${reports.count { it.status == "Resuelto" }}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}