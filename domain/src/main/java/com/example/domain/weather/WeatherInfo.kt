package com.example.domain.weather

data class WeatherInfo(
    //                     number of day,   weather
    //                          0         , today
    //                          1         , tomorrow ..
    val weatherDatePerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?

)
