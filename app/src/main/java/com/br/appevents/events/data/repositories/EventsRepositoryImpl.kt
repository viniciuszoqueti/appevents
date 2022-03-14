package com.br.appevents.events.data.repositories

import com.br.appevents.constants.RequestCodeConstants
import com.br.appevents.events.data.network.models.CheckinEventRequest
import com.br.appevents.events.data.network.models.EventDataResponse
import com.br.appevents.events.data.network.services.EventsService
import com.br.appevents.events.domain.repositories.EventsRepository
import com.br.appevents.events.domain.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsService: EventsService
) : EventsRepository {

    override suspend fun getEventsList(): Resource<List<EventDataResponse>> =
        withContext(Dispatchers.IO) {
            try {
                eventsService.getEventsList().let {
                    if (it.isSuccessful) {
                        it.body()?.let { data ->
                            return@withContext Resource.success(data)
                        }
                    }
                    return@withContext Resource.error(null, it.message())
                }

            } catch (ex: Exception) {
                return@withContext Resource.error(null, ex.message)
            }
        }


    override suspend fun getEventDetails(eventId: Int): Resource<EventDataResponse> =
        withContext(Dispatchers.IO) {
            try {
                eventsService.getEventDetails(eventId).let {
                    if (it.isSuccessful) {
                        it.body()?.let { data ->
                            return@withContext Resource.success(data)
                        }
                    } else if (it.code() == RequestCodeConstants.NOT_FOUND) {
                        return@withContext Resource.notFound(null)
                    }
                    return@withContext Resource.error(null, it.message())
                }

            } catch (ex: IOException) {
                return@withContext Resource.error(null, ex.message)
            }
        }

    override suspend fun checkinEvent(eventId: Int, name: String, email: String): Resource<Any> =
        withContext(Dispatchers.IO) {
            try {
                eventsService.checkinEvent(CheckinEventRequest(eventId, name, email)).let {
                    if (it.isSuccessful) {
                        it.body()?.let { data ->
                            return@withContext Resource.success(data)
                        }
                    }
                    return@withContext Resource.error(null, it.message())
                }

            } catch (ex: IOException) {
                return@withContext Resource.error(null, ex.message)
            }
        }

}