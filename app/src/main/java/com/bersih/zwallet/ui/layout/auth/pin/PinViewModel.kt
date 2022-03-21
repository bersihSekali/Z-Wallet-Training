package com.bersih.zwallet.ui.layout.auth.pin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bersih.zwallet.data.ZWalletDataSource
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.request.ChangePasswordRequest
import com.bersih.zwallet.model.request.SetPinRequest
import com.bersih.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(private val dataSource: ZWalletDataSource): ViewModel() {

    fun setPin(request: SetPinRequest): LiveData<Resource<ApiResponse<String>?>> {
        return dataSource.setPin(request)
    }

    fun changePassword(request: ChangePasswordRequest): LiveData<Resource<ApiResponse<String>?>> {
        return dataSource.changePassword(request)
    }

    fun checkPin(pin: String): LiveData<Resource<ApiResponse<String>?>> {
        return dataSource.checkPin(pin)
    }
}