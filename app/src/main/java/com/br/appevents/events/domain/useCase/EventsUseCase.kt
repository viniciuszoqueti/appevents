package com.br.appevents.events.domain.useCase

import com.br.appevents.events.domain.models.EventModelDomain
import com.br.appevents.events.domain.resource.Resource

interface EventsUseCase {
    suspend fun getEventsList(): Resource<List<EventModelDomain>>
    suspend fun getEventDetails(eventId: Int): Resource<EventModelDomain>
    suspend fun checkinEvent(eventId: Int, name: String, email: String): Resource<Any>
}