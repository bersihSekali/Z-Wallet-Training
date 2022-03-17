package com.bersih.zwallet.data

import androidx.lifecycle.liveData
import com.bersih.zwallet.data.api.ZWalletApi
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.request.LoginRequest
import com.bersih.zwallet.model.request.SetPinRequest
import com.bersih.zwallet.utils.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ZWalletDataSource @Inject constructor(private val apiClient: ZWalletApi) {
    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val loginRequest = LoginRequest(email = email, password = password)
            val response = apiClient.login(loginRequest)
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun getInvoice() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.getInvoice()
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun getBalance() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.getUserDetail()
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun getMyProfile() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.getMyProfile()
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun setPin(request: SetPinRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.setPin(request)
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }


}