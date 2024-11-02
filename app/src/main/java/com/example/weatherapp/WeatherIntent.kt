package com.example.weatherapp

sealed class WeatherIntent {
    object LoadWeatherInfo : WeatherIntent()
    data class SetError(val error: String) : WeatherIntent()
}