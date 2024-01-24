package com.example.gatherpoint.network

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/events/liked")
    suspend fun getSavedEvents(
        @Header("Authorization") token: String,
    ) : Response<List<Model.Event>>

    @GET("/events/created/{creatorId}")
    suspend fun getMyEvents(
        @Header("Authorization") token: String,
        @Path("creatorId") creatorId: Long,
    ) : Response<List<Model.Event>>

    @GET("/attendance/history")
    suspend fun getAttendanceHistory(
        @Header("Authorization") token: String,
        @Query("userId") userId: Long
    ) : Response<List<Model.Event>>

    @GET("/events/{id}")
    suspend fun getEvent(
        @Header("Authorization") token: String,
        @Path("id") eventId: Long,
    ) : Response<Model.Event>

    @GET("/auth/user")
    suspend fun getUser(
        @Header("Authorization") token: String,
    ) : Response<Model.User>

    @POST("/events")
    suspend fun addEvent(
        @Header("Authorization") token: String,
        @Body eventJson: JsonObject
    ) : Response<Model.Event>

    @PUT("/events/{id}")
    suspend fun editEvent(
        @Header("Authorization") token: String,
        @Path("id") eventId: Long,
        @Body eventJson: JsonObject
    ) : Response<Model.Event>

    @POST("/events/{id}/like")
    suspend fun addEventToFavourites(
        @Header("Authorization") token: String,
        @Path("id") eventId: Long
    ) : Response<Model.Event>

    @DELETE("/events/{id}/like")
    suspend fun removeItemFromFavourites(
        @Header("Authorization") token: String,
        @Path("id") eventId: Long
    ) : Response<Any>

    @DELETE("/events/{id}")
    suspend fun deleteEvent(
        @Header("Authorization") token: String,
        @Path("id") eventId: Long
    ) : Response<Model.Event>
}