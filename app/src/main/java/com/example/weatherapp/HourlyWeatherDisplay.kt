package com.example.weatherapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.weather.WeatherData
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyWeatherDisplay(
    weatherData: WeatherData,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    // Format time only when weather data changes
    val formattedTime = remember(weatherData) {
        weatherData.time.format(
            DateTimeFormatter.ofPattern("HH:mm")
        )
    }

    Column(
        modifier = modifier
            .width(80.dp)
            .height(120.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1B3B5A), Color(0xFF102840)),
                    startY = 0f,
                    endY = 300f
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display time with a lighter color and smaller font
        Text(
            text = formattedTime,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        // Main weather icon with padding to balance layout
        Image(
            painter = painterResource(id = weatherData.weatherType.iconRes),
            contentDescription = null,
            modifier = Modifier
                .width(36.dp)
                .height(36.dp)
                .padding(4.dp)
        )

        // Temperature text with bold styling
        Text(
            text = "${weatherData.temperatureCelsius}°C",
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
