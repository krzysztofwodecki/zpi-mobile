package com.example.gatherpoint.network

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("/auth/login")
    suspend fun login(
        @Body loginJson: JsonObject
    ) : Response<Any>

    @POST("/auth/register")
    suspend fun register(
        @Body registerJson: JsonObject
    ) : Response<Model.User>

    @GET("/events")
    suspend fun getEvents(
        @Header("Authorization") token: String,
    ) : Response<List<Model.Event>>
}