package com.example.jaddysgalvis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.data.repository.ReportRepository
import kotlinx.coroutines.launch

class ReportViewModel(
    private val repository: ReportRepository
) : ViewModel() {

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

    suspend fun getAllReports(): List<ReportEntity> {
        return repository.getAllReports()
    }
}