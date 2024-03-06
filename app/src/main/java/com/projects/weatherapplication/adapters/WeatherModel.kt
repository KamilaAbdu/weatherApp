package com.projects.weatherapplication.adapters

data class WeatherModel(
    val city: String,
    val localtime: String,
    val condition: String,
    val imageUrl: String,
    val temp: String,
    val hours: String,
    val uv: String,
    val maxWindKph: String,
    val humidity: String,
    val date: String
)
