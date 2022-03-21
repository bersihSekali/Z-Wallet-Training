package com.bersih.zwallet.model.request

import com.google.gson.annotations.SerializedName

data class ChangePinRequest(
    @SerializedName("old_password")
    val old_password: String,
    @SerializedName("new_password")
    val new_password: String
)
