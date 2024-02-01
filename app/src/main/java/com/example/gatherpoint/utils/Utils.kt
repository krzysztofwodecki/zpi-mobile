package com.example.gatherpoint.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

object Utils {
    fun mediator(vararg liveDataObjects: LiveData<*>) = MediatorLiveData<Any?>().apply {
        liveDataObjects.forEach { liveData -> addSource(liveData) { value = it } }
    }
}