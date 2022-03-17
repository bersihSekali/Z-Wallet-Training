package com.bersih.zwallet.ui.layout.auth.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bersih.zwallet.databinding.FragmentRegisterBinding
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.request.RegisterRequest
import com.bersih.zwallet.network.NetworkConfig
import com.bersih.zwallet.ui.layout.auth.AuthActivity
import com.bersih.zwallet.utils.*
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.btnSignUp.setOnClickListener {
            if (binding.inputUsername.text.isNullOrEmpty() || binding.inputEmail.text.isNullOrEmpty() || binding.inputPassword.text.isNullOrEmpty()) {
                Toast.makeText(activity, "Fill the required field!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registerRequest = RegisterRequest(
                binding.inputUsername.text.toString(),
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )

            NetworkConfig(context).buildApi().register(registerRequest)
                .enqueue(object : Callback<ApiResponse<String>> {
                    override fun onResponse(
                        call: Call<ApiResponse<String>>,
                        response: Response<ApiResponse<String>>
                    ) {
                        if (response.body()?.status != HttpsURLConnection.HTTP_OK) {
                            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Register success! Please login with your new account!", Toast.LENGTH_SHORT).show()

                            Handler().postDelayed({
                                val intent = Intent(activity, AuthActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }, 2000)
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}