package com.bersih.zwallet.ui.layout.auth.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bersih.zwallet.databinding.FragmentOtpBinding
import com.bersih.zwallet.ui.layout.SplashScreenActivity
import com.bersih.zwallet.ui.layout.auth.login.LoginViewModel
import com.bersih.zwallet.ui.widget.LoadingDialog
import com.bersih.zwallet.utils.KEY_REGIS_EMAIL
import com.bersih.zwallet.utils.PREFS_NAME
import com.bersih.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class OtpFragment : Fragment() {
    private lateinit var binding: FragmentOtpBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var preferences: SharedPreferences
    private lateinit var otp: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater)
        preferences = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareData(view)
    }

    private fun prepareData(view: View) {
        binding.btnConfirm.setOnClickListener {
            otp = binding.otp1.text.toString() + binding.otp2.text.toString() + binding.otp3.text.toString() +
                    binding.otp4.text.toString() + binding.otp5.text.toString() + binding.otp6.text.toString()

            email = preferences.getString(KEY_REGIS_EMAIL, "").toString()

            viewModel.setOtp(email, otp).observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Processing your request")
                    }
                    State.SUCCESS -> {
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            loadingDialog.start("Activated")

                            Handler().postDelayed({
                                val intent = Intent(activity, SplashScreenActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }, 2000)
                        } else {
                            loadingDialog.start("Invalid OTP!")
                        }
                    }
                    State.ERROR -> {
                        loadingDialog.stop()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}