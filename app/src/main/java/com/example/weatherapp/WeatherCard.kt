package com.example.weatherapp

import android.os.Build
import android.text.style.BackgroundColorSpan
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun weatherCard(
    state: WeatherState,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    state.weatherInfo?.currentWeatherData?.let { data ->
        Spacer(modifier = Modifier.height(3.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display time in a rounded background for more emphasis
                Text(
                    text = "Today ${data.time.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                    modifier = Modifier
                        .align(Alignment.End)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Main weather icon
                Image(
                    painter = painterResource(id = data.weatherType.iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .padding(8.dp)
                )

                // Temperature with larger, bolder font
                Text(
                    text = "${data.temperatureCelsius}°C",
                    fontSize = 56.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Weather description with a slight shadow
                Text(
                    text = data.weatherType.weatherDesc,
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(23.dp))

                // Row for weather data (Pressure, Humidity, Wind Speed)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherDataDisplay(
                        value = data.pressure.roundToInt(),
                        unit = "hpa",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_pressure),
                        iconTint = Color.White,
                        textStyle = TextStyle(color = Color.White)
                    )
                    WeatherDataDisplay(
                        value = data.humidity.roundToInt(),
                        unit = "%",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
                        iconTint = Color.White,
                        textStyle = TextStyle(color = Color.White)
                    )
                    WeatherDataDisplay(
                        value = data.windSpeed.roundToInt(),
                        unit = "km/h",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
                        iconTint = Color.White,
                        textStyle = TextStyle(color = Color.White)
                    )
                }
            }
        }
    }
}
