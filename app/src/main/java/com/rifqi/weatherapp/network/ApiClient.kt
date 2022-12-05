package com.rifqi.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiClient {

    fun apiClient() : ApiService {

        val client = okhttp3

        val retrofit = Retrofit.Builder()
            .client()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("")
            .build()

        return retrofit.create()
    }
}