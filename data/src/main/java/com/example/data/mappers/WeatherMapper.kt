package com.example.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.remote.WeatherDataDto
import com.example.data.remote.WeatherDto
import com.example.domain.weather.WeatherData
import com.example.domain.weather.WeatherInfo
import com.example.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                code = weatherCode,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
        //Each key in the resulting map is a day ,
        // (calculated by dividing the index by 24),
        // and each value is a list of weather data for that day.
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
//        val hour = if (now.minute < 30) now.hour else now.hour + 1


        val hour = when {
            now.minute < 30 -> now.hour
            now.hour == 23 -> 12.00
            else -> now.hour + 1
        }

        it.time.hour == hour
    }

    return WeatherInfo(
        weatherDatePerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}

