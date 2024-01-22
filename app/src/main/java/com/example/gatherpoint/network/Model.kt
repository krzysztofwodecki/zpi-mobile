package com.example.gatherpoint.network

object Model {

    data class Event(
        val id: Long,
        val creatorId: Long,
        val eventName: String,
        val eventDateTime: String,
        val location: String,
        val description: String
    )

    data class Reward(
        val id: Long,
        val name: String,
        val description: String,
        val value: Long
    )
}