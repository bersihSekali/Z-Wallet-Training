package com.bersih.zwallet.data

import androidx.lifecycle.liveData
import com.bersih.zwallet.data.api.ZWalletApi
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.GetInvoice
import com.bersih.zwallet.model.GetUserDetail
import com.bersih.zwallet.model.User
import com.bersih.zwallet.model.request.LoginRequest
import kotlinx.coroutines.Dispatchers

class ZWalletDataSource(private val apiClient: ZWalletApi) {
    fun login(email: String, password: String) = liveData<ApiResponse<User>>(Dispatchers.IO) {
        try {
            val loginRequest = LoginRequest(email = email, password = password)
            val response = apiClient.login(loginRequest)
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse(400, e.localizedMessage, null))
        }
    }

    fun getInvoice() = liveData<ApiResponse<List<GetInvoice>>>(Dispatchers.IO) {
        try {
            val response = apiClient.getInvoice()
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse(400, e.localizedMessage, null))
        }
    }

    fun getBalance() = liveData<ApiResponse<List<GetUserDetail>>>(Dispatchers.IO) {
        try {
            val response = apiClient.getUserDetail()
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse(400, e.localizedMessage, null))
        }
    }
}