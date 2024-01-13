package com.example.gatherpoint.utils

import android.app.Activity
import android.content.Context

class Prefs(activity: Activity) {
    private val preferences = activity.getPreferences(Context.MODE_PRIVATE)

    var userLoggedPref: Boolean
        get() = preferences.getBoolean(APP_PREF_USER_LOGGED, false)
        set(value) = preferences.edit().putBoolean(APP_PREF_USER_LOGGED, value).apply()

    companion object {
        private const val APP_PREF_USER_LOGGED = "userLoggedPref"
    }
}