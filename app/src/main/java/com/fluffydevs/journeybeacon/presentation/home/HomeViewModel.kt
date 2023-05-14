package com.fluffydevs.journeybeacon.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fluffydevs.journeybeacon.common.structure.BaseViewModel
import com.fluffydevs.journeybeacon.common.structure.Event
import com.fluffydevs.journeybeacon.data.login.LoginRequestBody
import com.fluffydevs.journeybeacon.data.payment.PaymentRequestBody
import com.fluffydevs.journeybeacon.domain.api.ServiceApi
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serviceApi: ServiceApi
) : BaseViewModel() {
    private var _paymentSuccess = MutableLiveData<Event<Unit>>()
    val paymentSuccess: LiveData<Event<Unit>>
        get() = _paymentSuccess

    private var _paymentFailure = MutableLiveData<Event<Unit>>()
    val paymentFailure: LiveData<Event<Unit>>
        get() = _paymentFailure

    suspend fun payTicket(account: GoogleSignInAccount, amount: String?, route: String?) {
        serviceApi.payTicket(
            PaymentRequestBody(
                userId = account.id ?: "",
                amount = amount ?: "",
                route = route ?: "",
                timestmp = System.currentTimeMillis().toString()
            )).let {
            when (it.isSuccessful) {
                true -> {
                    onPaymentSuccess()
                }

                else -> {
                    onPaymentFailed()
                }
            }
        }
    }

    private fun onPaymentSuccess() {
        _paymentSuccess.value = Event(Unit)
    }

    private fun onPaymentFailed() {
        _paymentFailure.value = Event(Unit)
    }
}