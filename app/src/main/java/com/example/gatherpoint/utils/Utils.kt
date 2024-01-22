package com.example.gatherpoint.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object Utils {
    fun mediator(vararg liveDataObjects: LiveData<*>) = MediatorLiveData<Any?>().apply {
        liveDataObjects.forEach { liveData -> addSource(liveData) { value = it } }
    }
}