package com.bersih.zwallet.ui.auth.pin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bersih.zwallet.data.ZWalletDataSource
import com.bersih.zwallet.data.api.ZWalletApi
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.request.SetPinRequest
import com.bersih.zwallet.network.NetworkConfig
import com.bersih.zwallet.utils.Resource

class PinViewModel(app: Application): ViewModel() {
    private var apiClient: ZWalletApi = NetworkConfig(app).buildApi()
    private var dataSource = ZWalletDataSource(apiClient)

    fun setPin(request: SetPinRequest): LiveData<Resource<ApiResponse<String>?>> {
        return dataSource.setPin(request)
    }
}