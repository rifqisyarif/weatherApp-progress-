package com.rifqi.weatherapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rifqi.weatherapp.network.ApiClient.apiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(){

    private val currentWeather =  MutableLiveData<CurrentWeatherResponse>()

    val getCurrentWeatherByCoordinate : LiveData<CurrentWeatherResponse> = currentWeather

    fun currentWeatherByCoordinate(lat : Double, long : Double) {

        apiClient().currentWeatherByCoordinate(lat, long).enqueue(object : Callback<CurrentWeatherResponse> {
            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                currentWeather.value = response.body()
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                Log.e("CURRENT_WEATHER", "onFailure: ${t.message}")
            }

        })
    }

    fun weatherByCurrentLocation(lat: Double, lon: Double) {
        ApiConfig.getApiService().weatherByCurrentLocation(lat, lon)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) weatherByCurrentLocation.postValue(response.body())
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getWeatherByCurrentLocation(): LiveData<WeatherResponse> = weatherByCurrentLocation

}