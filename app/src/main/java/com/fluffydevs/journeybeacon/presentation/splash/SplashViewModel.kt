package com.fluffydevs.journeybeacon.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fluffydevs.journeybeacon.common.structure.BaseViewModel
import com.fluffydevs.journeybeacon.common.structure.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {

    private var _navigateToLogin = MutableLiveData<Event<Unit>>()
    val navigateToLogin: LiveData<Event<Unit>>
        get() = _navigateToLogin

    init {
        checkIfLoggedIn()
    }

    private fun checkIfLoggedIn() {
        // TODO @Paul: check if logged in

        // TODO @Paul: if FALSE, and navigate to Login
        _navigateToLogin.postValue(Event(Unit))

        // TODO @Paul: if TRUE, and navigate to Home
    }
}