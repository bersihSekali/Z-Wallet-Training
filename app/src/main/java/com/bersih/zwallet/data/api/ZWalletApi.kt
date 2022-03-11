package com.bersih.zwallet.data.api

import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.GetUserDetail
import com.bersih.zwallet.model.User
import com.bersih.zwallet.model.request.GetUserDetailRequest
import com.bersih.zwallet.model.request.LoginRequest
import com.bersih.zwallet.model.request.RefreshTokenRequest
import com.bersih.zwallet.model.request.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ZWalletApi {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<ApiResponse<User>>

    @POST("auth/signup")
    fun register(@Body request: RegisterRequest): Call<ApiResponse<String>>

    @GET("home/getBalance")
    fun getUserDetail(): Call<ApiResponse<List<GetUserDetail>>>

    @POST("auth/refresh-token")
    fun refreshToken(@Body request: RefreshTokenRequest): Call<ApiResponse<User>>
}