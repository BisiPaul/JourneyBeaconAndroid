package com.fluffydevs.journeybeacon.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fluffydevs.journeybeacon.common.structure.BaseViewModel
import com.fluffydevs.journeybeacon.common.structure.Event
import com.fluffydevs.journeybeacon.data.payment.getpayments.PaymentModel
import com.fluffydevs.journeybeacon.domain.api.ServiceApi
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val serviceApi: ServiceApi
) : BaseViewModel() {
    private var _fetchPaymentsSuccess = MutableLiveData<Event<Unit>>()
    val fetchPaymentsSuccess: LiveData<Event<Unit>>
        get() = _fetchPaymentsSuccess

    private var _fetchPaymentsFailure = MutableLiveData<Event<Unit>>()
    val fetchPaymentsFailure: LiveData<Event<Unit>>
        get() = _fetchPaymentsFailure

    private val _paymentsLiveData = MutableLiveData<List<PaymentModel>>()
    val paymentsLiveData: LiveData<List<PaymentModel>?>
        get() = _paymentsLiveData

    suspend fun getPayments(account: GoogleSignInAccount) {
        serviceApi.getPayments(account.id ?: "").let {
            when (it.isSuccessful) {
                true -> {
                    _paymentsLiveData.value = it.body()?.payments
                    _fetchPaymentsSuccess.value = Event(Unit)
                }
                false -> {
                    _fetchPaymentsFailure.value = Event(Unit)
                }
            }
        }
    }
}