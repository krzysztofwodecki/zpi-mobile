package com.example.gatherpoint.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.network.RetrofitHelper
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel (application: Application): AndroidViewModel(application) {

    private val api = RetrofitHelper.getInstance()

    private val _loginState = MutableLiveData<Resource<String>>()
    val loginState: LiveData<Resource<String>> = _loginState

    private val _registrationState = MutableLiveData<Resource<Model.User?>>()
    val registrationState: LiveData<Resource<Model.User?>> = _registrationState
    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        _loginState.postValue(Resource.Loading())

        val loginJson = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }
        val response = api.login(loginJson)
        if (response.isSuccessful && response.body() != null) {
            _loginState.postValue(Resource.Success(response.body().toString()))
        } else {
            _loginState.postValue(Resource.Error("Invalid email or password"))
        }
    }

    fun register(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        _registrationState.postValue(Resource.Loading())

        val registerJson = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }
        val response = api.register(registerJson)
        if (response.isSuccessful && response.body() != null) {
            _registrationState.postValue(Resource.Success(response.body()))
        } else {
            _registrationState.postValue(Resource.Error("Cannot register this user"))
        }
    }

}