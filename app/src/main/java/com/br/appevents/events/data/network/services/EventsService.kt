package com.br.appevents.events.data.network.services

import com.br.appevents.events.data.network.models.CheckinEventRequest
import com.br.appevents.events.data.network.models.EventDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventsService {

    @GET("events")
    suspend fun getEventsList(): Response<List<EventDataResponse>>

    @GET("events/{eventId}")
    suspend fun getEventDetails(@Path("eventId") eventId: Int): Response<EventDataResponse>

    @POST("checkin")
    suspend fun checkinEvent(@Body checkinEventRequest: CheckinEventRequest): Response<Any>

}