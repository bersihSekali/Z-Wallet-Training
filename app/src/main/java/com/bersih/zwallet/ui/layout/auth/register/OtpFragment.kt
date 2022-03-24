package com.bersih.zwallet.ui.layout.auth.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
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
    private lateinit var keyOtp: String
    private lateinit var email: String
    var otp  = mutableListOf<EditText>()

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

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        editTextOtp()
        deleteText()

        binding.otp6.addTextChangedListener {
            if (binding.otp6.text.length > 0) {
                binding.btnConfirm.setBackgroundResource(R.drawable.background_button_auth_active)
                binding.btnConfirm.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (binding.otp6.text.length == 0) {
                binding.btnConfirm.setBackgroundResource(R.drawable.background_button_auth)
                binding.btnConfirm.setTextColor(Color.parseColor("#9DA6B5"))
            }
        }

        prepareData(view)
    }

    private fun prepareData(view: View) {
        binding.btnConfirm.setOnClickListener {
            keyOtp = binding.otp1.text.toString() + binding.otp2.text.toString() + binding.otp3.text.toString() +
                    binding.otp4.text.toString() + binding.otp5.text.toString() + binding.otp6.text.toString()

            email = preferences.getString(KEY_REGIS_EMAIL, "").toString()

            if (binding.otp6.text.isNullOrEmpty()) {
                Toast.makeText(activity, "Enter otp code!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.setOtp(email, keyOtp).observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Activing your account")
                        Toast.makeText(context, "Activing your account", Toast.LENGTH_LONG).show()
                    }
                    State.SUCCESS -> {
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            loadingDialog.start("Account activated")
                            Toast.makeText(context, "Account activated", Toast.LENGTH_LONG).show()
                            val intent = Intent(activity, SplashScreenActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        } else {
                            loadingDialog.start("Activation failed")
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                    State.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun editTextOtp() {
        otp.add(0, binding.otp1)
        otp.add(1, binding.otp2)
        otp.add(2, binding.otp3)
        otp.add(3, binding.otp4)
        otp.add(4, binding.otp5)
        otp.add(5, binding.otp6)
        otpHandler()
    }

    fun otpHandler() {
        for(i in 0..5) {
            otp.get(i).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    otp.get(i).setBackgroundResource(R.drawable.background_edit_text_after)
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (i ==5 && !otp.get(i).getText().toString().isEmpty()) {
                        otp.get(i).clearFocus()
                    } else if (!otp.get(i).getText().toString().isEmpty()) {
                        otp.get(i + 1).requestFocus()
                    }
                }
            })

//            otp.get(i).setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
//                if (keyEvent.action !== KeyEvent.ACTION_DOWN) {
//                    return@OnKeyListener false
//                }
//                if (i == KeyEvent.KEYCODE_DEL && otp.get(i).getText().toString().isEmpty() && i != 0) {
//                    otp.get(i - 1).setText("")
//                    otp.get(i - 1).requestFocus()
//                    otp.get(i - 1).setBackgroundResource(R.drawable.background_edit_text)
//                }
//                false
//            })
        }
    }

    private fun deleteText() {
        binding.otp6.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.otp5.requestFocus()
                binding.otp6.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.otp5.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.otp4.requestFocus()
                binding.otp5.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.otp4.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.otp3.requestFocus()
                binding.otp4.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.otp3.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.otp2.requestFocus()
                binding.otp3.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.otp2.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.otp1.requestFocus()
                binding.otp2.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.otp1.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.otp1.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
    }


}