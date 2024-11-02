package com.example.weatherapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.DarkBlue
import com.example.weatherapp.ui.theme.DeepBlue
import com.example.weatherapp.ui.theme.WeatherAppTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                viewModel.sendIntent(WeatherIntent.LoadWeatherInfo)  // Load weather if location is granted
                // Load weather only if location is granted
            } else {
                // Handle the case where location permission is denied
                viewModel.sendIntent(WeatherIntent.SetError("Location permission denied."))
            }
        }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            WeatherAppTheme {
                val state by viewModel.state.collectAsState()

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkBlue)
                    ) {
                        weatherCard(
                            state = state,
                            backgroundColor = DeepBlue,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        WeatherForecast(
                            state = state,
                        )
                    }

                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }

                    state.error?.let { error ->
                        Image(
                            painter = painterResource(id = R.drawable.error),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(120.dp),
                        )


                        LaunchedEffect(error) {
                            Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                        }

                        if (
                            state.error == "Couldn't retrieve location. Make sure to grant permission and enable GPS." &&
                            state.error != "Location permission denied."
                        ) {
                            Button(
                                onClick = {
                                    viewModel.sendIntent(WeatherIntent.LoadWeatherInfo)
                                },
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(top = 170.dp)
                            ) {
                                Text("Retry")
                            }
                        }

                    }
                }

            }
        }
    }
}