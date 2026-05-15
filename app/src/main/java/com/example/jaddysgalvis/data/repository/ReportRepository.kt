package com.example.jaddysgalvis.data.repository

import com.example.jaddysgalvis.data.local.dao.ReportDao
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.data.remote.dto.ReportDto
import com.example.jaddysgalvis.data.remote.retrofit.RetrofitClient

class ReportRepository(
    private val reportDao: ReportDao
) {

    // ROOM

    suspend fun getAllReports(): List<ReportEntity> {

        return reportDao.getAllReports()
    }

    suspend fun insertReport(report: ReportEntity) {

        reportDao.insertReport(report)
    }

    suspend fun updateReport(report: ReportEntity) {

        reportDao.updateReport(report)
    }

    suspend fun deleteReport(report: ReportEntity) {

        reportDao.deleteReport(report)
    }

    // API

    suspend fun getRemoteReports(): List<ReportDto>? {

        val response =
            RetrofitClient.api.getReports()

        return if (response.isSuccessful) {

            response.body()

        } else {

            null
        }
    }

    suspend fun sendReportToApi(
        reportDto: ReportDto
    ): Boolean {

        val response =
            RetrofitClient.api.createReport(reportDto)

        return response.isSuccessful
    }
}