package com.rifqi.weatherapp.utils

import java.math.RoundingMode

fun degreeToCelsius(temp: Double?): String? {
    val castTemp = temp as Double
    val tempToCelsius = castTemp - 273.0
    val formatDegree = tempToCelsius.toBigDecimal()
        .setScale(2, RoundingMode.CEILING).toDouble()
    return "$formatDegree°C"
}