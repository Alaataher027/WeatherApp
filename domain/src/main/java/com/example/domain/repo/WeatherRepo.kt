package com.example.domain.repo

import com.example.domain.util.Resource
import com.example.domain.weather.WeatherInfo

interface WeatherRepo {
    suspend fun getWeatherData(lat: Double, long: Double) : Resource<WeatherInfo>
}