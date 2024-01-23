package com.example.gatherpoint.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @POST("/auth/login")
    suspend fun login() : Response<Any>
}