package com.fluffydevs.journeybeacon.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fluffydevs.journeybeacon.common.structure.BaseViewModel
import com.fluffydevs.journeybeacon.common.structure.Event
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
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
    }

    fun onSignOut() {
        _signOutCompleted.value = Event(Unit)
    }
}