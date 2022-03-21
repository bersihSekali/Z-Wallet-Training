package com.bersih.zwallet.model

import com.google.gson.annotations.SerializedName


data class CheckPin(
    @SerializedName("PIN")
    val pin: String
)