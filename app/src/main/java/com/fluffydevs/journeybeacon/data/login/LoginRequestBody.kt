package com.fluffydevs.journeybeacon.data.login

data class LoginRequestBody(
    val email: String,
    val userId: String,
    val idToken: String,
    val displayName: String
)