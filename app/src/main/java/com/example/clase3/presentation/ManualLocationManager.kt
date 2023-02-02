package com.example.clase3.presentation

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.clase3.presentation.data.WeatherApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class ManualLocationManager() {

    val dataLoaded = mutableStateOf(false)

    val data = mutableStateOf(
        CardData(
            weatherInfo = "",
            time = "", name = "", temp = 0.0
        )
    )

    fun createLocationRequest(
        lon: Double,
        lat: Double,
sharedPreferences: SharedPreferences,
        navController: NavController
        ) {
        val data = mutableStateOf(
            CardData(
                weatherInfo = "",
                time = "", name = "", temp = 0.0
            )
        )
        CoroutineScope(Dispatchers.Main).launch {
            val weatherDTO = WeatherApi.apiInstance.getWeatherDetails(
                lat,
                lon, "71caf2c7c215536c260ae4f58e9209d0")

            var editor = sharedPreferences.edit()
            editor.putString("weatherInfo", weatherDTO.weather[0].description)
            editor.commit()
            editor.putString("time", "${(weatherDTO.main.temp - 273).roundToInt()}°C")
            editor.commit()
            editor.putString("name", weatherDTO.name)
            editor.commit()
            editor.putString("temp", (weatherDTO.main.temp - 273).roundToInt().toDouble().toString())
            editor.commit()
            Log.i("TAG", "parametros: ${weatherDTO.weather[0].description}")
            Log.i("TAG", "parametros: ${weatherDTO.name}")
            Log.i("TAG", "parametros: ${(weatherDTO.main.temp - 273).roundToInt()}°C")
            Log.i("TAG", "parametros: ${(weatherDTO.main.temp - 273).roundToInt().toDouble()})}")
            navController.navigate(NavRoute.PARAMETROS)
        }
            }

    }





