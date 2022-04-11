package com.br.appevents.events.presentation.event_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appevents.events.domain.models.Event
import com.br.appevents.events.domain.repositories.EventsRepository
import com.br.appevents.events.domain.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val _eventLiveData: MutableLiveData<Resource<Event>> = MutableLiveData()
    val eventLiveData: LiveData<Resource<Event>> get() = _eventLiveData

    private val _checkinLiveData: MutableLiveData<Resource<Any>> = MutableLiveData()
    val checkinLiveData: LiveData<Resource<Any>> get() = _checkinLiveData

    fun getEventById(eventId: Int) {
        viewModelScope.launch {
            _eventLiveData.postValue(Resource.Loading())
            try {
                _eventLiveData.postValue(eventsRepository.getEventDetails(eventId))
            } catch (ex: Exception) {
                _eventLiveData.postValue(Resource.Error(ex.message))
            }
        }
    }

    fun checkin(eventId: Int, name: String, email: String) {
        viewModelScope.launch {
            _checkinLiveData.postValue(Resource.Loading())
            try {
                _checkinLiveData.postValue(eventsRepository.checkinEvent(eventId, name, email))
            } catch (ex: Exception) {
                _checkinLiveData.postValue(Resource.Error(ex.message))
            }
        }
    }

    fun validationFormCheckin(name: String, email: String) = name.length > 3 && email.length > 5

}