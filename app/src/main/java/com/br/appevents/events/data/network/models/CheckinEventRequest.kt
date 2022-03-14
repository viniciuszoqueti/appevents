package com.br.appevents.events.data.network.models

data class CheckinEventRequest(
    val eventId: Int,
    val name: String,
    val email: String,
)
