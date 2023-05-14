package com.fluffydevs.journeybeacon.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fluffydevs.journeybeacon.common.structure.BaseViewModel
import com.fluffydevs.journeybeacon.common.structure.Event
import com.fluffydevs.journeybeacon.data.login.LoginRequestBody
import com.fluffydevs.journeybeacon.domain.api.ServiceApi
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val serviceApi: ServiceApi
) : BaseViewModel() {
    private var _loginSuccess = MutableLiveData<Event<Unit>>()
    val loginSuccess: LiveData<Event<Unit>>
        get() = _loginSuccess

    private var _loginFailure = MutableLiveData<Event<Unit>>()
    val loginFailure: LiveData<Event<Unit>>
        get() = _loginFailure


    suspend fun loginBackend(account: GoogleSignInAccount) {
        serviceApi.login(LoginRequestBody(
            email = account.email ?: "",
            userId = account.id ?: "",
            idToken = account.idToken ?: "",
            displayName = account.displayName ?: ""
        )).let {
            when (it.isSuccessful) {
                true -> {
                    onLoginSuccess()
                }
                else -> {
                    onLoginFailure()
                }
            }
        }
    }

    fun onLoginSuccess() {
        _loginSuccess.value = Event(Unit)

    }

    fun onLoginFailure() {
        _loginFailure.value = Event(Unit)
    }
}