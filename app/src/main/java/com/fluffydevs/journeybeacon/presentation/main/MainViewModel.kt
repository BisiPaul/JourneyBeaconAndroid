package com.fluffydevs.journeybeacon.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fluffydevs.journeybeacon.common.structure.BaseViewModel
import com.fluffydevs.journeybeacon.common.structure.Event
import com.fluffydevs.journeybeacon.data.login.LoginRequestBody
import com.fluffydevs.journeybeacon.domain.api.ServiceApi
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val serviceApi: ServiceApi
) : BaseViewModel() {
    lateinit var account: GoogleSignInAccount

    private var _userAlreadySignedIn = MutableLiveData<Event<Unit>>()
    val userAlreadySignedIn: LiveData<Event<Unit>>
        get() = _userAlreadySignedIn

    private var _newUser = MutableLiveData<Event<Unit>>()
    val newUser: LiveData<Event<Unit>>
        get() = _newUser

    private var _signOutCompleted = MutableLiveData<Event<Unit>>()
    val signOutCompleted: LiveData<Event<Unit>>
        get() = _signOutCompleted

    private var _hasEnteredRegion = MutableLiveData<Event<Unit>>()
    val hasEnteredRegion: LiveData<Event<Unit>>
        get() = _hasEnteredRegion

    private var _hasExitedRegion = MutableLiveData<Event<Unit>>()
    val hasExitedRegion: LiveData<Event<Unit>>
        get() = _hasExitedRegion

    fun onNewUser() {
        _newUser.value = Event(Unit)
    }

    fun onUserAlreadySignedIn(account: GoogleSignInAccount) {
        this.account = account
        _userAlreadySignedIn.value = Event(Unit)
        viewModelScope.launch {
            // TODO @Paul: add request body
            loginBackend()
        }
    }

    private suspend fun loginBackend() {
        serviceApi.login(
            LoginRequestBody(
                email = account.email ?: "",
                userId = account.id ?: "",
                idToken = account.idToken ?: "",
                displayName = account.displayName ?: ""
            )
        )
    }

    fun onSignOut() {
        _signOutCompleted.value = Event(Unit)
        // TODO @Paul: add request body
        viewModelScope.launch {
            serviceApi.logout()
        }
    }

    fun onRegionEnter() {
        _hasEnteredRegion.value = Event(Unit)
    }

    fun onRegionExit() {
        _hasExitedRegion.value = Event(Unit)
    }
}