package com.br.appevents.events.data.repositories

import com.br.appevents.constants.RequestCodeConstants
import com.br.appevents.events.data.network.models.CheckinEventRequest
import com.br.appevents.events.data.network.models.toDomain
import com.br.appevents.events.data.network.services.EventsService
import com.br.appevents.events.domain.models.EventModelDomain
import com.br.appevents.events.domain.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsService: EventsService
) : EventsRepository {

    override suspend fun getEventsList(): Resource<List<EventModelDomain>> =
        withContext(Dispatchers.IO) {
            try {
                eventsService.getEventsList().let {
                    if (it.isSuccessful) {
                        it.body()?.let { data ->
                            return@withContext Resource.Success(data.toDomain())
                        }
                    }

                    return@withContext Resource.Error(it.message())
                }
            } catch (ex: Exception) {
                return@withContext Resource.Error(ex.message)
            }
        }


    override suspend fun getEventDetails(eventId: Int): Resource<EventModelDomain> =
        withContext(Dispatchers.IO) {
            try {
                eventsService.getEventDetails(eventId).let {
                    if (it.isSuccessful) {
                        it.body()?.let { data ->
                            return@withContext Resource.Success(data.toDomain())
                        }
                    } else if (it.code() == RequestCodeConstants.NOT_FOUND) {
                        return@withContext Resource.NotFound()
                    }
                    Resource.Error(it.message())
                }

            } catch (ex: IOException) {
                Resource.Error(ex.message)
            }
        }

    override suspend fun checkinEvent(eventId: Int, name: String, email: String): Resource<Any> =
        withContext(Dispatchers.IO) {
            try {
                eventsService.checkinEvent(CheckinEventRequest(eventId, name, email)).let {
                    if (it.isSuccessful) {
                        it.body()?.let { data ->
                            return@withContext Resource.Success(data)
                        }
                    }
                    Resource.Error(it.message())
                }
            } catch (ex: IOException) {
                Resource.Error(ex.message)
            }
        }

}