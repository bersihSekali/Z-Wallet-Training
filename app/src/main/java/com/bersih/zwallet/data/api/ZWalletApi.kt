package com.bersih.zwallet.data.api

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import com.bersih.zwallet.model.*
import com.bersih.zwallet.model.request.*
import com.bersih.zwallet.utils.Resource
import retrofit2.Call
import retrofit2.http.*

interface ZWalletApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<User>

    @POST("auth/signup")
    fun register(@Body request: RegisterRequest): Call<ApiResponse<String>>

    @GET("auth/activate/{email}/{otp}")
    suspend fun setOtp(@Path("email") email: String, @Path("otp") otp: String): ApiResponse<String>

    @GET("home/getBalance")
    suspend fun getUserDetail(): ApiResponse<List<GetUserDetail>>

    @GET("home/getInvoice")
    suspend fun getInvoice(): ApiResponse<List<GetInvoice>>

    @GET("tranfer/contactUser")
    suspend fun getContact(): ApiResponse<List<GetContact>>

    @POST("auth/refresh-token")
    fun refreshToken(@Body request: RefreshTokenRequest): Call<ApiResponse<User>>

    @GET("user/myProfile")
    suspend fun getMyProfile(): ApiResponse<PersonalProfile>

    @PATCH("auth/PIN")
    suspend fun setPin(@Body request: SetPinRequest): ApiResponse<String>

    @GET("auth/checkPIN/{PIN}")
    suspend fun checkPin(@Path("PIN") pin: String): ApiResponse<String>

    @POST("tranfer/newTranfer")
    suspend fun transfer(
        @Body request: TransferRequest,
        @Header("x-access-PIN") pin: String
    ): ApiResponse<Transfer>

    @PATCH("user/changePassword")
    suspend fun changePassword(@Body request: ChangePasswordRequest): ApiResponse<String>

    @PATCH("topup/topupbalance")
    suspend fun topUpBalance(@Body request: TopUpBalanceRequest): ApiResponse<String>

    @PATCH("user/changeInfo")
    suspend fun editPhone(@Body request: EditPhoneRequest): ApiResponse<String>
}