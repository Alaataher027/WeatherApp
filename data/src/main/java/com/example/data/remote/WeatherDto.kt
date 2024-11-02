package com.example.data.remote

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class WeatherDto(
    @field:SerializedName("hourly")
    val weatherData: WeatherDataDto
) : Parcelable

