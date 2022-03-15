package com.bersih.zwallet.model.request

import com.google.gson.annotations.SerializedName

data class SetPinRequest(
    @SerializedName("PIN")
    val pin: String
)
