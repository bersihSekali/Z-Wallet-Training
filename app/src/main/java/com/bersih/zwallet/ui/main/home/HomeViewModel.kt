package com.bersih.zwallet.ui.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bersih.zwallet.data.ZWalletDataSource
import com.bersih.zwallet.data.api.ZWalletApi
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.GetInvoice
import com.bersih.zwallet.model.GetUserDetail
import com.bersih.zwallet.network.NetworkConfig

class HomeViewModel(app: Application): ViewModel() {
    private var apiClient: ZWalletApi = NetworkConfig(app).buildApi()
    private var dataSource = ZWalletDataSource(apiClient)

    fun getInvoice(): LiveData<ApiResponse<List<GetInvoice>>> {
        return dataSource.getInvoice()
    }

    fun getBalance(): LiveData<ApiResponse<List<GetUserDetail>>> {
        return dataSource.getBalance()
    }
}