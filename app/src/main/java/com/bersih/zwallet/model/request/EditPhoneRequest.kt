package com.bersih.zwallet.model.request


import com.google.gson.annotations.SerializedName

data class EditPhoneRequest(
    @SerializedName("phone")
    val phone: String?
)