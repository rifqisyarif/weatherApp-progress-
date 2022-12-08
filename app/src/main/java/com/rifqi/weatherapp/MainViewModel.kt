package com.rifqi.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rifqi.weatherapp.model.CurrentWeatherResponse
import com.rifqi.weatherapp.model.ForecastWeatherResponse
import com.rifqi.weatherapp.network.ApiClient
import com.rifqi.weatherapp.network.ApiClient.apiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(){

    private val currentWeather =  MutableLiveData<CurrentWeatherResponse>()
    private val forecastWeather =  MutableLiveData<ForecastWeatherResponse>()

    val getCurrentWeatherByCoordinate : LiveData<CurrentWeatherResponse> = currentWeather
    val getForecastWeatherByCoordinate : LiveData<ForecastWeatherResponse> = forecastWeather

    private val currentWeatherByCity =  MutableLiveData<CurrentWeatherResponse>()
    private val forecastWeatherByCity =  MutableLiveData<ForecastWeatherResponse>()

    val getCurrentWeatherByCity : LiveData<CurrentWeatherResponse> = currentWeatherByCity
    val getForecastWeatherByCity : LiveData<ForecastWeatherResponse> = forecastWeatherByCity

    fun currentWeatherByCoordinate(lat : Double, long : Double) {

        apiClient().currentWeatherByCoordinate(lat, long).enqueue(object : Callback<CurrentWeatherResponse> {
            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                currentWeather.value = response.body() // Cara pertama memasukan value didalam MutableLiveData
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                Log.e("CURRENT_WEATHER", "onFailure: ${t.message}")
            }

        })
    }

    fun forecastWeatherByCoordinate(lat: Double, lon: Double) {
        apiClient().forecastByCurrentLocation(lat, lon).enqueue(object : Callback<ForecastWeatherResponse> {
            override fun onResponse(
                call: Call<ForecastWeatherResponse>,
                response: Response<ForecastWeatherResponse>
            ) {
                forecastWeather.postValue(response.body()) // Cara kedua memasukan value didalam MutableLiveData
            }

            override fun onFailure(call: Call<ForecastWeatherResponse>, t: Throwable) {
                Log.e("FORECAST_WEATHER", "onFailure: ${t.message}")
            }

        })
    }

    fun currentWeatherByCity(city: String) {
        apiClient().currentWeatherByCity(city).enqueue(object : Callback<CurrentWeatherResponse> {
            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                currentWeatherByCity.postValue(response.body())
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                Log.i("CURRENT_WEATHER", "onFailure: ${t.message} ")
            }

        })
    }

    fun forecastWeatherByCity(city: String) {
        apiClient().forecastWeatherByCity(city).enqueue(object : Callback<ForecastWeatherResponse> {
            override fun onResponse(
                call: Call<ForecastWeatherResponse>,
                response: Response<ForecastWeatherResponse>
            ) {
                forecastWeatherByCity.postValue(response.body())
            }

            override fun onFailure(call: Call<ForecastWeatherResponse>, t: Throwable) {
                Log.i("FORECAST_WEATHER", "onFailure: ${t.message} ")
            }

        })
    }
}