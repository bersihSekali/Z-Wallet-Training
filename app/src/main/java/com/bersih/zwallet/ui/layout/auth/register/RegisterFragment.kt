package com.bersih.zwallet.ui.layout.auth.register

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentRegisterBinding
import com.bersih.zwallet.model.request.RegisterRequest
import com.bersih.zwallet.ui.layout.auth.login.LoginViewModel
import com.bersih.zwallet.ui.widget.LoadingDialog
import com.bersih.zwallet.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        binding.inputPassword.addTextChangedListener {
            if (binding.inputPassword.text.length > 8) {
                binding.btnSignUp.setBackgroundResource(R.drawable.background_button_auth_active)
                binding.btnSignUp.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (binding.inputPassword.text.length <= 8) {
                binding.btnSignUp.setBackgroundResource(R.drawable.background_button_auth)
                binding.btnSignUp.setTextColor(Color.parseColor("#9DA6B5"))
            }
        }

        binding.btnSignUp.setOnClickListener {
            if (binding.inputUsername.text.isNullOrEmpty() || binding.inputEmail.text.isNullOrEmpty()
                || binding.inputPassword.text.isNullOrEmpty()) {
                Toast.makeText(activity, "Fill the required field!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registerRequest = viewModel.register(
                binding.inputUsername.toString(),
                binding.inputEmail.toString(),
                binding.inputPassword.toString()
            )

            prefs.edit {
                putString(KEY_REGIS_EMAIL, binding.inputEmail.text.toString())
                apply()
            }

            registerRequest.observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Processing your request ;)")
                    }
                    State.SUCCESS -> {
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            loadingDialog.stop()
                            Toast.makeText(context, "Check your email", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_otpFragment)
                        } else {
                            loadingDialog.stop()
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    State.ERROR -> {
                        loadingDialog.stop()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}