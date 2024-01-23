package com.example.gatherpoint.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.utils.Utils.mediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EventsViewModel(application: Application): AndroidViewModel(application) {

    private val _event = MutableLiveData<Resource<Model.Event>>()
    val event: LiveData<Resource<Model.Event>> = _event

    private val nearEvents = MutableLiveData<Resource<List<Model.Event>>>()
    private val nearEventsSearchQuery = MutableLiveData("")

    val nearEventsList = mediator(nearEvents, nearEventsSearchQuery).map {
        val nearEventsResource = nearEvents.value ?: return@map null
        val nearEventsSearchQuery = nearEventsSearchQuery.value ?: ""

        if (nearEventsResource is Resource.Success) {
            Resource.Success(nearEventsResource.data?.filter {
                it.eventName.contains(nearEventsSearchQuery, ignoreCase = true) }
            )
        } else {
            nearEventsResource
        }
    }

    private val savedEvents = MutableLiveData<Resource<List<Model.Event>>>()
    private val savedEventsSearchQuery = MutableLiveData("")

    val savedEventsList = mediator(savedEvents, savedEventsSearchQuery).map {
        val savedEventsResource = savedEvents.value ?: return@map null
        val savedEventsSearchQuery = savedEventsSearchQuery.value ?: ""

        if (savedEventsResource is Resource.Success) {
            Resource.Success(savedEventsResource.data?.filter {
                it.eventName.contains(savedEventsSearchQuery, ignoreCase = true) }
            )
        } else {
            savedEventsResource
        }
    }

    private val myEvents = MutableLiveData<Resource<List<Model.Event>>>()
    private val myEventsSearchQuery = MutableLiveData("")

    val myEventsList = mediator(myEvents, myEventsSearchQuery).map {
        val myEventsResource = myEvents.value ?: return@map null
        val myEventsSearchQuery = myEventsSearchQuery.value ?: ""

        if (myEventsResource is Resource.Success) {
            Resource.Success(myEventsResource.data?.filter {
                it.eventName.contains(myEventsSearchQuery, ignoreCase = true) }
            )
        } else {
            myEventsResource
        }
    }

    fun getNearEventsList() {
        nearEvents.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            nearEvents.postValue(Resource.Success(EventProvider.getEventList()))
        }
    }

    fun getSavedEventsList() {
        savedEvents.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            savedEvents.postValue(Resource.Success(EventProvider.getEventList()))
        }
    }

    fun getMyEventsList() {
        myEvents.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            myEvents.postValue(Resource.Success(EventProvider.getEventList()))
        }
    }

    fun setNearEventsSearchQuery(query: String) {
        nearEventsSearchQuery.value = query
    }
    fun setSavedEventsSearchQuery(query: String) {
        savedEventsSearchQuery.value = query
    }
    fun setMyEventsSearchQuery(query: String) {
        myEventsSearchQuery.value = query
    }

    fun addEventToFavourites(eventId: Long) {
        TODO("Not yet implemented")
    }

    fun removeEventToFavourites(eventId: Long) {
        TODO("Not yet implemented")
    }

    fun deleteEvent(eventId: Long) {
        TODO("Not yet implemented")
    }

    fun getEventById(eventId: Long) {
        _event.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            _event.postValue(Resource.Success(EventProvider.getEventList()[0]))
        }
    }

    object EventProvider {
        fun getEventList(): List<Model.Event> {
            return listOf(
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                ),
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                ),
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                ),
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                ),
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                ),
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                ),
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                ),
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                ),
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                ),
                Model.Event(
                    1,
                    3,
                    "Zielona Sobota: Sadzenie Drzew dla Przyszłości",
                    "2023-12-28T21:55:56.815799",
                    "Park Krakowski",
                    "Dołącz do nas w \"Sadzeniu Drzew: Nasz Wspólny Gest dla Ziemi\"! Spotkajmy się tego dnia, by razem zasadzić nowe życie i zadbać o naszą planetę. Rozpoczynamy edukacyjnymi prelekcjami, a potem przechodzimy do praktyki, sadząc drzewa w miejscowym parku. To prosta, ale ważna inicjatywa, która buduje wspólnotę i dba o środowisko. Przyłącz się, a razem uczynimy naszą przyszłość bardziej zieloną i zrównoważoną!"
                )
            )
        }
    }
}