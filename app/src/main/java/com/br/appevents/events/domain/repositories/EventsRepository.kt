package com.br.appevents.events.domain.repositories

import com.br.appevents.events.data.network.models.EventDataResponse
import com.br.appevents.events.domain.resource.Resource

interface EventsRepository {

    suspend fun getEventsList(): Resource<List<EventDataResponse>>
    suspend fun getEventDetails(eventId: Int): Resource<EventDataResponse>
    suspend fun checkinEvent(eventId: Int, name: String, email: String): Resource<Any>

}