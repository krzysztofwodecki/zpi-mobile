package com.example.gatherpoint.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.network.RetrofitHelper
import com.example.gatherpoint.utils.Utils.mediator
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventsViewModel(application: Application) : AndroidViewModel(application) {

    private val api = RetrofitHelper.getInstance()

    private val _event = MutableLiveData<Resource<Model.Event>?>()
    val event: LiveData<Resource<Model.Event>?> = _event

    private val _eventActionStatus = MutableLiveData<String?>()
    val eventActionStatus: LiveData<String?> = _eventActionStatus

    private val _eventDetailsStatus = MutableLiveData<Resource<Model.Event>?>()
    val eventDetailsStatus: LiveData<Resource<Model.Event>?> = _eventDetailsStatus

    private val nearEvents = MutableLiveData<Resource<List<Model.Event>>>()
    private val nearEventsSearchQuery = MutableLiveData("")

    val nearEventsList = mediator(nearEvents, nearEventsSearchQuery).map {
        val nearEventsResource = nearEvents.value ?: return@map null
        val nearEventsSearchQuery = nearEventsSearchQuery.value ?: ""

        if (nearEventsResource is Resource.Success) {
            Resource.Success(nearEventsResource.data?.filter {
                it.eventName.contains(nearEventsSearchQuery, ignoreCase = true)
            }
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
                it.eventName.contains(savedEventsSearchQuery, ignoreCase = true)
            }
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
                it.eventName.contains(myEventsSearchQuery, ignoreCase = true)
            }
            )
        } else {
            myEventsResource
        }
    }

    fun getNearEventsList(token: String) = viewModelScope.launch(Dispatchers.IO) {
        nearEvents.postValue(Resource.Loading())
        val response = api.getEvents("Bearer $token")
        if (response.isSuccessful && response.body() != null) {
            nearEvents.postValue(Resource.Success(response.body()!!))
        }
    }

    fun getSavedEventsList(token: String) = viewModelScope.launch(Dispatchers.IO) {
        savedEvents.postValue(Resource.Loading())
        val response = api.getSavedEvents("Bearer $token")
        if (response.isSuccessful && response.body() != null) {
            savedEvents.postValue(Resource.Success(response.body()!!))
        }
    }

    fun getMyEventsList(token: String, userId: Long) = viewModelScope.launch(Dispatchers.IO) {
        myEvents.postValue(Resource.Loading())
        val response = api.getMyEvents("Bearer $token", userId)
        if (response.isSuccessful && response.body() != null) {
            myEvents.postValue(Resource.Success(response.body()!!))
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

    fun addEventToFavourites(token: String, eventId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val response = api.addEventToFavourites("Bearer $token", eventId)
        if (response.isSuccessful) {
            _eventActionStatus.postValue("Event added to saved")
            getSavedEventsList(token)
        } else {
            _eventActionStatus.postValue("Cannot add event to saved")
        }
    }

    fun clearEventActionStatus() {
        _eventActionStatus.value = null
    }

    fun removeEventFromFavourites(token: String, eventId: Long) =
        viewModelScope.launch(Dispatchers.IO) {
            val response = api.removeItemFromFavourites("Bearer $token", eventId)
            if (response.isSuccessful) {
                _eventActionStatus.postValue("Event removed from saved")
                getSavedEventsList(token)
            } else {
                _eventActionStatus.postValue("Cannot remove item from saved")
            }
        }

    fun deleteEvent(token: String, userId: Long, eventId: Long) =
        viewModelScope.launch(Dispatchers.IO) {
            val response = api.deleteEvent("Bearer $token", eventId)
            if (response.isSuccessful) {
                _eventActionStatus.postValue("Event deleted")
                getSavedEventsList(token)
                getNearEventsList(token)
                getMyEventsList(token, userId)
            } else {
                _eventActionStatus.postValue("Cannot delete event")
            }
        }

    fun getEventById(token: String, eventId: Long) = viewModelScope.launch(Dispatchers.IO) {
        _event.postValue(Resource.Loading())
        val response = api.getEvent("Bearer $token", eventId)
        if (response.isSuccessful && response.body() != null) {
            _event.postValue(Resource.Success(response.body()!!))
        }
    }

    fun clearEvent() {
        _event.value = null
    }

    fun addEvent(
        token: String,
        userId: Long,
        title: String,
        location: String,
        description: String,
        date: String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (date == null) return@launch
        _eventDetailsStatus.postValue(Resource.Loading())

        val eventJson = JsonObject().apply {
            addProperty("creatorId", userId)
            addProperty("eventName", title)
            addProperty("eventDateTime", date)
            addProperty("location", location)
            addProperty("description", description)
        }
        val response = api.addEvent("Bearer $token", eventJson)
        if (response.isSuccessful && response.body() != null) {
            _eventDetailsStatus.postValue(Resource.Success(response.body()!!))
            getMyEventsList(token, userId)
            getNearEventsList(token)
        } else {
            _eventDetailsStatus.postValue(Resource.Error("Cannot create event"))
        }
    }

    fun editEvent(
        token: String,
        eventId: Long,
        userId: Long,
        title: String,
        location: String,
        description: String,
        date: String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (date == null) return@launch
        _eventDetailsStatus.postValue(Resource.Loading())

        val eventJson = JsonObject().apply {
            addProperty("eventName", title)
            addProperty("eventDateTime", date)
            addProperty("location", location)
            addProperty("description", description)
        }
        val response = api.editEvent("Bearer $token", eventId, eventJson)
        if (response.isSuccessful && response.body() != null) {
            _eventDetailsStatus.postValue(Resource.Success(response.body()!!))
            getMyEventsList(token, userId)
            getNearEventsList(token)
        } else {
            _eventDetailsStatus.postValue(Resource.Error("Cannot edit event"))
        }
    }

    fun clearEventStatus() {
        _eventDetailsStatus.value = null
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