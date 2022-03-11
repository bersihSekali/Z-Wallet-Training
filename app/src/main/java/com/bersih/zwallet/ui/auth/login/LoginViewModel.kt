package com.bersih.zwallet.ui.auth.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.bersih.zwallet.data.api.ZWalletApi
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.User
import com.bersih.zwallet.model.request.LoginRequest
import com.bersih.zwallet.network.NetworkConfig
import com.bersih.zwallet.utils.*
import javax.net.ssl.HttpsURLConnection

class LoginViewModel(app: Application): AndroidViewModel(app) {
    private var preferences: SharedPreferences = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private var apiClient: ZWalletApi = NetworkConfig(app).buildApi()

    fun login(email: String, password: String): ApiResponse<User>? {
        val loginRequest = LoginRequest(email = email, password = password)
        val response = apiClient.login(loginRequest).execute()
        if (response.isSuccessful) {
            if (response.body()?.status == HttpsURLConnection.HTTP_OK) {
                val res = response.body()!!.data

                with(preferences.edit()) {
                    putBoolean(KEY_LOGGED_IN, true)
                    putString(KEY_USER_EMAIL, res?.email)
                    putString(KEY_USER_TOKEN, res?.token)
                    putString(KEY_USER_REFRESH_TOKEN, res?.refreshToken)
                    apply()
                }
            }
        }
        return response.body()
    }
}