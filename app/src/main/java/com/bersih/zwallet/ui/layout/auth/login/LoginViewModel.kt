package com.bersih.zwallet.ui.layout.auth.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bersih.zwallet.data.ZWalletDataSource
import com.bersih.zwallet.data.api.ZWalletApi
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.User
import com.bersih.zwallet.network.NetworkConfig
import com.bersih.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dataSource: ZWalletDataSource): ViewModel() {

    fun login(email: String, password: String): LiveData<Resource<ApiResponse<User>?>> {
        return dataSource.login(email, password)
    }
}