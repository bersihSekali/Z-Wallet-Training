package com.bersih.zwallet.ui.layout.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bersih.zwallet.data.ZWalletDataSource
import com.bersih.zwallet.model.*
import com.bersih.zwallet.model.request.GetContact
import com.bersih.zwallet.model.request.TransferRequest
import com.bersih.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val dataSource: ZWalletDataSource): ViewModel() {
    private var selectedContact = MutableLiveData<GetContact>()
    private var transfer = MutableLiveData<TransferRequest>()

    fun getBalance(): LiveData<Resource<ApiResponse<List<GetUserDetail>>?>> {
        return dataSource.getBalance()
    }

    fun getContact(): LiveData<com.bersih.zwallet.utils.Resource<ApiResponse<List<GetContact>>?>> {
        return dataSource.getContact()
    }

    fun setSelectedContact(contact: GetContact) {
        selectedContact.value = contact
    }

    fun selectedContact(): MutableLiveData<GetContact>{
        return selectedContact
    }

    fun getTransferParam(): MutableLiveData<TransferRequest>{
        return transfer
    }

    fun setTransferParam(data: TransferRequest) {
        transfer.value = data
    }

    fun transfer(data: TransferRequest, pin: String): LiveData<Resource<ApiResponse<Transfer>?>> {
        return dataSource.transfer(data, pin)
    }
}