package com.bersih.zwallet.ui.layout.auth.register

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentRegisterBinding
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.request.RegisterRequest
import com.bersih.zwallet.network.NetworkConfig
import com.bersih.zwallet.utils.KEY_REGIS_EMAIL
import com.bersih.zwallet.utils.PREFS_NAME
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var prefs : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        binding.textSignIn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.inputPassword.addTextChangedListener {
            if (binding.inputPassword.text.length >= 8) {
                binding.btnSignUp.setBackgroundResource(R.drawable.background_button_auth_active)
                binding.btnSignUp.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (binding.inputPassword.text.length < 8) {
                binding.btnSignUp.setBackgroundResource(R.drawable.background_button_auth)
                binding.btnSignUp.setTextColor(Color.parseColor("#9DA6B5"))
            }
        }

        binding.btnSignUp.setOnClickListener {
            if (binding.inputUsername.text.isNullOrEmpty() ||
                binding.inputEmail.text.isNullOrEmpty() ||
                binding.inputPassword.text.isNullOrEmpty()
            ) {
                Toast.makeText(activity, "Must be fill", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (binding.inputPassword.text.length < 8) {
                Toast.makeText(activity, "Password must at least 8 character", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registRequest = RegisterRequest(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString(),
                binding.inputUsername.text.toString()
            )
            prefs.edit {
                putString(KEY_REGIS_EMAIL, binding.inputEmail.text.toString())
                apply()
            }
            NetworkConfig(context).buildApi().register(registRequest)
                .enqueue(object : Callback<ApiResponse<String>> {
                    override fun onResponse(
                        call: Call<ApiResponse<String>>,
                        response: Response<ApiResponse<String>>
                    ) {
                        if (response.body()?.status != HttpsURLConnection.HTTP_OK) {
                            Toast.makeText(context, "Authentication failed: Invalid", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Authentication Success. Check your email", Toast.LENGTH_LONG).show()
                            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_otpFragment)
                        }
                    }
                    override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}