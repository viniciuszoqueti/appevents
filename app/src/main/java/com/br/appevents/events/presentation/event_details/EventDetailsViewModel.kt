package com.br.appevents.events.presentation.event_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appevents.events.domain.models.toPresentation
import com.br.appevents.events.domain.resource.Resource
import com.br.appevents.events.domain.useCase.EventsUseCase
import com.br.appevents.events.presentation.models.EventModelPresentation
import com.br.appevents.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase
) : ViewModel() {

    private val _eventLiveData: SingleLiveEvent<Resource<EventModelPresentation>> =
        SingleLiveEvent()
    val eventLiveData: LiveData<Resource<EventModelPresentation>> get() = _eventLiveData

    private val _checkinLiveData: SingleLiveEvent<Resource<Any>> = SingleLiveEvent()
    val checkinLiveData: LiveData<Resource<Any>> get() = _checkinLiveData

    fun getEventById(eventId: Int) {
        viewModelScope.launch {
            _eventLiveData.postValue(Resource.Loading())
            try {
                val response = eventsUseCase.getEventDetails(eventId).castResource {
                    when (it) {
                        is Resource.Success -> Resource.Success(it.data.toPresentation())
                        is Resource.Error -> Resource.Error(it.msg)
                        is Resource.Loading -> Resource.Loading()
                        else -> Resource.NotFound()
                    }
                }
                _eventLiveData.postValue(response)
            } catch (ex: Exception) {
                _eventLiveData.postValue(Resource.Error(ex.message))
            }
        }
    }

    fun checkin(eventId: Int, name: String, email: String) {
        viewModelScope.launch {
            _checkinLiveData.postValue(Resource.Loading())
            try {
                _checkinLiveData.postValue(eventsUseCase.checkinEvent(eventId, name, email))
            } catch (ex: Exception) {
                _checkinLiveData.postValue(Resource.Error(ex.message))
            }
        }
    }

    fun validationFormCheckin(name: String, email: String) = name.length > 3 && email.length > 5

}