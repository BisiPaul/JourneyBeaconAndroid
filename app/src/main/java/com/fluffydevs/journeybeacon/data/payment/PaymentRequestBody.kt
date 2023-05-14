package com.fluffydevs.journeybeacon.data.payment

data class PaymentRequestBody(
    val userId: String,
    val amount: String,
    val route: String,
    val timestmp: String
)