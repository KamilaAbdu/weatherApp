package com.projects.weatherapplication

data class DayItem(
    val city: String,
    val time: String,
    val condition: String,
    val imageUrl: String,
    val maxTemp: String,
    val minTemp: String,
    val hours: String,
)
