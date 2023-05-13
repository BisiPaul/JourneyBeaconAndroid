package com.fluffydevs.journeybeacon.domain.api

import com.fluffydevs.journeybeacon.data.login.LoginResponse
import com.fluffydevs.journeybeacon.data.logout.LogoutResponse
import retrofit2.Response
import retrofit2.http.POST

interface ServiceApi {
    @POST("/login")
    suspend fun login(): Response<LoginResponse>

    @POST("/logout")
    suspend fun logout(): Response<LogoutResponse>
}