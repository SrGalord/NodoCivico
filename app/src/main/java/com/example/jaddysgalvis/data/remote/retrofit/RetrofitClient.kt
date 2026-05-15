package com.example.jaddysgalvis.data.remote.retrofit

import com.example.jaddysgalvis.data.remote.api.ReportApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // CAMBIA ESTA IP POR LA TUYA

    private const val BASE_URL =
        "http://192.168.1.13:5000/"

    val api: ReportApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ReportApi::class.java)
    }
}