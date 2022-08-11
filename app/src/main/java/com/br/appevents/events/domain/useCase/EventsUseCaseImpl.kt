package com.br.appevents.events.domain.useCase

import com.br.appevents.events.data.repositories.EventsRepository
import com.br.appevents.events.domain.models.EventModelDomain
import com.br.appevents.events.domain.resource.Resource
import javax.inject.Inject

class EventsUseCaseImpl @Inject constructor(
    private val eventsRepository: EventsRepository
) : EventsUseCase {

    override suspend fun getEventsList(): Resource<List<EventModelDomain>> =
        eventsRepository.getEventsList()

    override suspend fun getEventDetails(eventId: Int): Resource<EventModelDomain> =
        eventsRepository.getEventDetails(eventId)

    override suspend fun checkinEvent(eventId: Int, name: String, email: String): Resource<Any> =
        eventsRepository.checkinEvent(eventId, name, email)
}