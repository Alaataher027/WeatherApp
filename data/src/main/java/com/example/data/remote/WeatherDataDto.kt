package com.example.data.remote

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class WeatherDataDto(
    @field:SerializedName("time")
    val time: List<String>,

    @field:SerializedName("temperature_2m")
    val temperatures: List<Double>,

    @field:SerializedName("weathercode")
    val weatherCodes: List<Int>,

    @field:SerializedName("pressure_msl")
    val pressures: List<Double>,

    @field:SerializedName("windspeed_10m")
    val windSpeeds: List<Double>,

    @field:SerializedName("relativehumidity_2m")
    val humidities: List<Double>
) : Parcelable
