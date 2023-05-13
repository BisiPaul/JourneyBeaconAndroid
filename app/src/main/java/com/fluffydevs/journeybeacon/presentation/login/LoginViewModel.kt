package com.fluffydevs.journeybeacon.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fluffydevs.journeybeacon.common.structure.BaseViewModel
import com.fluffydevs.journeybeacon.common.structure.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {
    private var _loginSuccess = MutableLiveData<Event<Unit>>()
    val loginSuccess: LiveData<Event<Unit>>
        get() = _loginSuccess

    private var _loginFailure = MutableLiveData<Event<Unit>>()
    val loginFailure: LiveData<Event<Unit>>
        get() = _loginFailure


    fun onLoginSuccess() {
        _loginSuccess.value = Event(Unit)
    }

    fun onLoginFailure() {
        _loginFailure.value = Event(Unit)
    }
}