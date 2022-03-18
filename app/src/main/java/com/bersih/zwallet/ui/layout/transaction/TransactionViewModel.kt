package com.bersih.zwallet.ui.layout.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bersih.zwallet.data.ZWalletDataSource
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.request.GetContact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionModel @Inject constructor(private val dataSource: ZWalletDataSource): ViewModel() {
    fun getContact(): LiveData<com.bersih.zwallet.utils.Resource<ApiResponse<List<GetContact>>?>> {
        return dataSource.getContact()
    }
}