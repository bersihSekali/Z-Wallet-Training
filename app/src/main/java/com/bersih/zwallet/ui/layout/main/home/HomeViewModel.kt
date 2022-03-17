package com.bersih.zwallet.ui.layout.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bersih.zwallet.data.ZWalletDataSource
import com.bersih.zwallet.data.api.ZWalletApi
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.GetInvoice
import com.bersih.zwallet.model.GetUserDetail
import com.bersih.zwallet.model.PersonalProfile
import com.bersih.zwallet.network.NetworkConfig
import com.bersih.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val dataSource: ZWalletDataSource): ViewModel() {

    fun getInvoice(): LiveData<Resource<ApiResponse<List<GetInvoice>>?>> {
        return dataSource.getInvoice()
    }

    fun getBalance(): LiveData<Resource<ApiResponse<List<GetUserDetail>>?>> {
        return dataSource.getBalance()
    }

    fun getMyProfile(): LiveData<Resource<ApiResponse<PersonalProfile>?>> {
        return dataSource.getMyProfile()
    }
}