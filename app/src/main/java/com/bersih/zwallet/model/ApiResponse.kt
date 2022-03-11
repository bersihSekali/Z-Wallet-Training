package com.bersih.zwallet.model

//200 suskse
//401 error

data class ApiResponse<T>(
    var status: Int,
    var messages: String,
    var data: T?
)
