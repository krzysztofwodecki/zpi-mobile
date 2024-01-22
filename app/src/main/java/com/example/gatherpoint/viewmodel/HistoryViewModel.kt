package com.example.gatherpoint.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HistoryViewModel (application: Application): AndroidViewModel(application) {

    private val historyEvents = MutableLiveData<Resource<List<Model.Event>>>()
    private val historyEventsSearchQuery = MutableLiveData("")

    val historyEventsList = Utils.mediator(historyEvents, historyEventsSearchQuery).map {
        val historyEventsResource = historyEvents.value ?: return@map null
        val historyEventsSearchQuery = historyEventsSearchQuery.value ?: ""

        if (historyEventsResource is Resource.Success) {
            Resource.Success(historyEventsResource.data?.filter {
                it.eventName.contains(historyEventsSearchQuery, ignoreCase = true) }
            )
        } else {
            historyEventsResource
        }
    }

    fun getHistoryEventsList() {
        historyEvents.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            historyEvents.postValue(Resource.Success(EventsViewModel.EventProvider.getEventList()))
        }
    }

    fun setHistoryEventsSearchQuery(query: String) {
        historyEventsSearchQuery.value = query
    }

    fun addEventToFavourites(eventId: Long) {
        TODO("Not yet implemented")
    }

}