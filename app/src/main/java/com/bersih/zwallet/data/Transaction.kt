package com.bersih.zwallet.data

import android.graphics.drawable.Drawable

data class Transaction(
    val transactionImg: Drawable,
    val transactionName: String,
    val transactionType: String,
    val transactionNominal: Double
)
