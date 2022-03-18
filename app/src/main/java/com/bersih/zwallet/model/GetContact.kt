package com.bersih.zwallet.model.request


import com.google.gson.annotations.SerializedName

data class GetContact(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?
)