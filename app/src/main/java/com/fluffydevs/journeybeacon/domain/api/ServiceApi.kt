package com.fluffydevs.journeybeacon.domain.api

import com.fluffydevs.journeybeacon.data.login.LoginRequestBody
import com.fluffydevs.journeybeacon.data.logout.LogoutResponse
import com.fluffydevs.journeybeacon.data.payment.PaymentRequestBody
import com.fluffydevs.journeybeacon.data.payment.getpayments.GetPaymentsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiceApi {
    @POST("/api/v1/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body body: LoginRequestBody): Response<Any>

    @POST("/logout")
    suspend fun logout(): Response<LogoutResponse>

    @POST("/api/v1/payments")
    @Headers("Content-Type: application/json")
    suspend fun payTicket(@Body body: PaymentRequestBody): Response<Any>

    @GET("/api/v1/users/{accountId}")
    @Headers("Content-Type: application/json")
    suspend fun getPayments(@Path("accountId") accountId: String): Response<GetPaymentsResponse>
}