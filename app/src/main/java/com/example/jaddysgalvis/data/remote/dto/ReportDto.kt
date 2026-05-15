package com.example.jaddysgalvis.data.remote.dto

data class ReportDto(

    val id: Int = 0,

    val title: String,

    val description: String,

    val category: String,

    val priority: String,

    val status: String,

    val location: String,

    val date: String
)