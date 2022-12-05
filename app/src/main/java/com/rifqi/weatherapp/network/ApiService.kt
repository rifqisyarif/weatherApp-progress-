package com.rifqi.weatherapp.network

import com.rifqi.weatherapp.model.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    fun currentWeatherByCoordinate(
        @Query("lat") latitude: Double,
        @Query("lon") longtitude: Double
    ) : CurrentWeatherResponse
}