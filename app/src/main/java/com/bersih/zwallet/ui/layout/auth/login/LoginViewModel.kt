package com.bersih.zwallet.ui.layout.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bersih.zwallet.data.ZWalletDataSource
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.User
import com.bersih.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dataSource: ZWalletDataSource): ViewModel() {
    fun login(email: String, password: String): LiveData<Resource<ApiResponse<User>?>> {
        return dataSource.login(email, password)
    }

    fun register(username: String, email: String, password: String): LiveData<Resource<ApiResponse<String>?>> {
        return dataSource.register(username, email, password)
    }

    fun setOtp(email: String, otp: String): LiveData<Resource<ApiResponse<String>?>> {
        return  dataSource.setOtp(email, otp)
    }
}