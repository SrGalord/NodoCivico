package com.example.jaddysgalvis.data.remote.api

import com.example.jaddysgalvis.data.remote.dto.ReportDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReportApi {

    @GET("reports")
    suspend fun getReports(): Response<List<ReportDto>>

    @POST("reports")
    suspend fun createReport(
        @Body report: ReportDto
    ): Response<ReportDto>
}