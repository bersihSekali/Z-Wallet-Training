package com.bersih.zwallet.network

import android.content.Context
import com.bersih.zwallet.utils.KEY_USER_TOKEN
import com.bersih.zwallet.utils.PREFS_NAME
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(val token: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            request.header("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}