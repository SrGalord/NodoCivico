package com.example.jaddysgalvis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val status: String,
    val location: String,
    val date: String
)