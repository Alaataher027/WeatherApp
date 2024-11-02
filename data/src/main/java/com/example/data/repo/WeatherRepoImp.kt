package com.example.data.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.mappers.toWeatherInfo
import com.example.data.remote.WeatherApi
import com.example.domain.repo.WeatherRepo
import com.example.domain.util.Resource
import com.example.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepoImp @Inject constructor(
    private val apiService: WeatherApi
) : WeatherRepo {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = apiService.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown Error occurred.")
        }
    }
}