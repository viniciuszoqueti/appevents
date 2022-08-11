package com.br.appevents.events.presentation.events_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.appevents.events.domain.models.toPresentation
import com.br.appevents.events.domain.resource.Resource
import com.br.appevents.events.domain.useCase.EventsUseCase
import com.br.appevents.events.presentation.models.EventModelPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase
) : ViewModel() {

    private val _eventsListLiveData:
            MutableLiveData<Resource<List<EventModelPresentation>>> = MutableLiveData()
    val eventsListLiveData: LiveData<Resource<List<EventModelPresentation>>> get() = _eventsListLiveData

    fun loadEvents() {
        viewModelScope.launch {
            _eventsListLiveData.postValue(Resource.Loading())
            try {
                val response = eventsUseCase.getEventsList().castResource {
                    when (it) {
                        is Resource.Success -> Resource.Success(it.data.toPresentation())
                        is Resource.Error -> Resource.Error(it.msg)
                        is Resource.Loading -> Resource.Loading()
                        else -> Resource.NotFound()
                    }
                }
                _eventsListLiveData.postValue(response)
            } catch (ex: Exception) {
                _eventsListLiveData.postValue(Resource.Error(ex.message))
            }
        }
    }

}
