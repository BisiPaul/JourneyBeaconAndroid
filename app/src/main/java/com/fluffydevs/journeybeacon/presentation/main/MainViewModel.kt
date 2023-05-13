package com.fluffydevs.journeybeacon.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fluffydevs.journeybeacon.common.structure.BaseViewModel
import com.fluffydevs.journeybeacon.common.structure.Event
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

    fun onNewUser() {
        _newUser.value = Event(Unit)
    }

    fun onUserAlreadySignedIn(account: GoogleSignInAccount) {
        this.account = account
        _userAlreadySignedIn.value = Event(Unit)
        viewModelScope.launch {
            // TODO @Paul: add request body
            serviceApi.login()
        }
    }

    fun onSignOut() {
        _signOutCompleted.value = Event(Unit)
        // TODO @Paul: add request body
        viewModelScope.launch {
            serviceApi.logout()
        }
    }
}