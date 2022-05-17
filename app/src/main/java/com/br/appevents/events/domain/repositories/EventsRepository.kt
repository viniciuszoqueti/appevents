package com.br.appevents.events.domain.repositories

import com.br.appevents.events.data.network.models.EventDataResponse
import com.br.appevents.events.domain.models.Event
import com.br.appevents.events.domain.resource.Resource

interface EventsRepository {

    suspend fun getEventsList(): Resource<List<Event>>
    suspend fun getEventDetails(eventId: Int): Resource<Event>
    suspend fun checkinEvent(eventId: Int, name: String, email: String): Resource<Any>

}