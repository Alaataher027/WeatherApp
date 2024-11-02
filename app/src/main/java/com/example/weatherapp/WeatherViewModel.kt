package com.example.weatherapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.remote.WeatherDataDto
import com.example.domain.location.LocationTracker
import com.example.domain.repo.WeatherRepo
import com.example.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.internal.Intrinsics.Kotlin

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repo: WeatherRepo,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    //LiveData
//    var state by mutableStateOf(WeatherState())
//        private set  // Public getter, private setter

    // intents channel
    private val intentChannel = Channel<WeatherIntent>(Channel.UNLIMITED)

    init {
        // Collect intents from the channel
        viewModelScope.launch {
            intentChannel.receiveAsFlow().collect { intent ->
                when (intent) {
                    is WeatherIntent.LoadWeatherInfo -> loadWeatherInfo()
                    is WeatherIntent.SetError -> setStateError(intent.error)
                }
            }
        }
    }

    // Function to offer intents to the channel
    fun sendIntent(intent: WeatherIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    fun setStateError(error: String) {
        _state.value = _state.value.copy(error = error)
    }

    fun loadWeatherInfo() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                when (val result = repo.getWeatherData(location.altitude, location.longitude)) {
                    is Resource.Success -> {
                        Log.d("WeatherViewModel", "code: ${result.data?.currentWeatherData?.code}")
                        Log.d("WeatherRepo", "Data fetched: ${result.data}")

                        _state.value = _state.value.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                        Log.d("WeatherViewModel", "Weather data loaded successfully!")

                    }

                    is Resource.Error -> {
                        Log.e("WeatherRepo", "Error fetching data: ${result.message}")

                        _state.value = _state.value.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                        Log.e("WeatherViewModel", "Failed to load weather data: ${result.message}")

                    }
                }

            } ?: kotlin.run {  // if we don't get a location
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
                Log.e("WeatherViewModel", "Location not available.")

            }
        }
    }
}