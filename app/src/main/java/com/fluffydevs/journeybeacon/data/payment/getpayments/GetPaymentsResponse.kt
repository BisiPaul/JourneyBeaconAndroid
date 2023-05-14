package com.fluffydevs.journeybeacon.data.payment.getpayments

data class GetPaymentsResponse(
    val displayName: String,
    val payments: List<PaymentModel>
)