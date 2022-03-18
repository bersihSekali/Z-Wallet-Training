package com.bersih.zwallet.model.request

import android.graphics.drawable.Drawable

data class GetContactRequest(
    val id: Int,
    val image: Drawable,
    val name: String,
    val phone: String
)
