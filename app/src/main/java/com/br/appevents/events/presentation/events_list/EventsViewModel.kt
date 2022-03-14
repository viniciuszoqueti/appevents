package com.br.appevents.events.presentation.events_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appevents.events.data.network.models.EventDataResponse
import com.br.appevents.events.domain.repositories.EventsRepository
import com.br.appevents.events.domain.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) :
    ViewModel() {

    private val _eventsListLiveData:
            MutableLiveData<Resource<List<EventDataResponse>>> = MutableLiveData()
    val eventsListLiveData: LiveData<Resource<List<EventDataResponse>>> get() = _eventsListLiveData

    fun loadEvents() {
        viewModelScope.launch {
            _eventsListLiveData.postValue(Resource.loading(null))
            try {
                _eventsListLiveData.postValue(eventsRepository.getEventsList())
            } catch (ex: Exception) {
                _eventsListLiveData.postValue(Resource.error(null, ex.message))
            }
        }
    }

}
