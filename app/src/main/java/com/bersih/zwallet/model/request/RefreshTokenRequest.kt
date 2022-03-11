package com.bersih.zwallet.model.request

data class RefreshTokenRequest(
    val email: String,
    val refreshToken: String
)
