package com.example.gatherpoint.network

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val baseUrl = "http://localhost:8080"

    fun getInstance(): ApiInterface {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(Gson().newBuilder().setLenient().create()))
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
            .create(ApiInterface::class.java)
    }
}