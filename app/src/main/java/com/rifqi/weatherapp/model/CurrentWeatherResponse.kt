package com.rifqi.weatherapp.model

import com.google.gson.annotations.SerializedName

// POJO (Plain Old Java Object)
data class CurrentWeatherResponse(

	@field:SerializedName("main")
	val main: Main? = null,

	@field:SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,
)

