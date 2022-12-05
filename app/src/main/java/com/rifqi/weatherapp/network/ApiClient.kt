package com.rifqi.weatherapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {

    fun apiClient() : ApiService {

        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .pingInterval(10, TimeUnit.SECONDS)
            .addInterceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                return@addInterceptor it.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()

        return retrofit.create(ApiService::class.java)
    }
}