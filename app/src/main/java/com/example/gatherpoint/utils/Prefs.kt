package com.example.gatherpoint.utils

import android.app.Activity
import android.content.Context

class Prefs(activity: Activity) {
    private val preferences = activity.getPreferences(Context.MODE_PRIVATE)

    var token: String?
        get() = preferences.getString(APP_PREF_USER_TOKEN, null)
        set(value) = preferences.edit().putString(APP_PREF_USER_TOKEN, value).apply()

    var userId: Long
        get() = preferences.getLong(APP_PREF_USER_ID, -1)
        set(value) = preferences.edit().putLong(APP_PREF_USER_ID, value).apply()

    companion object {
        private const val APP_PREF_USER_TOKEN = "userToken"
        private const val APP_PREF_USER_ID = "userId"
    }
}