package com.bersih.zwallet.model


import com.google.gson.annotations.SerializedName

data class GetUserDetail(
    @SerializedName("email")
    val email: String?,
    @SerializedName("lastname")
    val lastName: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("balance")
    val balance: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?
)