package com.rifqi.weatherapp.network

import com.rifqi.weatherapp.model.CurrentWeatherResponse
import com.rifqi.weatherapp.model.ForecastWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    fun currentWeatherByCoordinate(
        @Query("lat") latitude: Double,
        @Query("lon") longtitude: Double,
        @Query("units") tempStandart: String = "metric",
        @Query("appid") apiKey: String = "c174d83af9ea1aeb265705d9b4f7cac4"
    ) : Call<CurrentWeatherResponse>

    @GET("forecast")
    fun forecastByCurrentLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") tempStandart: String = "metric",
        @Query("appid") apiKey: String = "c174d83af9ea1aeb265705d9b4f7cac4"
    ) : Call<ForecastWeatherResponse>

    @GET("weather")
    fun currentWeatherByCity(
        @Query("q") city: String,
        @Query("units") tempStandart: String = "metric",
        @Query("appid") apiKey: String = "c174d83af9ea1aeb265705d9b4f7cac4"
    ) : Call<CurrentWeatherResponse>

    @GET("forecast")
    fun forecastWeatherByCity(
        @Query("q") city: String,
        @Query("units") tempStandart: String = "metric",
        @Query("appid") apiKey: String = "c174d83af9ea1aeb265705d9b4f7cac4"
    ) : Call<ForecastWeatherResponse>
}