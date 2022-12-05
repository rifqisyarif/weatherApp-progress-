package com.rifqi.weatherapp.network

import com.rifqi.weatherapp.model.CurrentWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    fun currentWeatherByCoordinate(
        @Query("lat") latitude: Double,
        @Query("lon") longtitude: Double,
        @Query("appid") apiKey: String = "c174d83af9ea1aeb265705d9b4f7cac4"
    ) : Call<CurrentWeatherResponse>
}