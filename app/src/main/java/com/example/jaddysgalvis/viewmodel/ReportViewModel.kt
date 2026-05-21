package com.example.jaddysgalvis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.data.repository.ReportRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReportViewModel(
    private val repository: ReportRepository
) : ViewModel() {

    // ✅ Estado observable seguro para UI
    val reports: StateFlow<List<ReportEntity>> =
        repository.getAllReports()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // ---------------- CRUD ----------------

    fun insertReport(report: ReportEntity) {
        viewModelScope.launch {
            repository.insertReport(report)
        }
    }

    fun updateReport(report: ReportEntity) {
        viewModelScope.launch {
            repository.updateReport(report)
        }
    }

    fun deleteReport(report: ReportEntity) {
        viewModelScope.launch {
            repository.deleteReport(report)
        }
    }
}