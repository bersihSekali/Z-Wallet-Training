package com.bersih.zwallet.model.request

import com.google.gson.annotations.SerializedName

data class TopUpBalanceRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("amount")
    val amount: String
)
