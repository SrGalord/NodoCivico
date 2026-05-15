package com.example.jaddysgalvis.data.local.dao

import androidx.room.*
import com.example.jaddysgalvis.data.local.entity.ReportEntity

@Dao
interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Update
    suspend fun updateReport(report: ReportEntity)

    @Delete
    suspend fun deleteReport(report: ReportEntity)

    @Query("SELECT * FROM reports ORDER BY id DESC")
    suspend fun getAllReports(): List<ReportEntity>

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: Int): ReportEntity
}