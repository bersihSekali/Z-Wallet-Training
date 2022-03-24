package com.bersih.zwallet.ui.layout.auth.password

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentChangePasswordBinding
import com.bersih.zwallet.model.request.ChangePasswordRequest
import com.bersih.zwallet.ui.layout.SplashScreenActivity
import com.bersih.zwallet.ui.layout.auth.pin.PinViewModel
import com.bersih.zwallet.ui.widget.LoadingDialog
import com.bersih.zwallet.utils.KEY_LOGGED_IN
import com.bersih.zwallet.utils.PREFS_NAME
import com.bersih.zwallet.utils.State
import javax.net.ssl.HttpsURLConnection

class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var preferences: SharedPreferences
    private val viewModel: PinViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPassword.addTextChangedListener {
            if (binding.newPassword.text.length >= 8) {
                binding.btnContinue.setBackgroundResource(R.drawable.background_button_auth_active)
                binding.btnContinue.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (binding.newPassword.text.length < 8) {
                binding.btnContinue.setBackgroundResource(R.drawable.background_button_auth)
                binding.btnContinue.setTextColor(Color.parseColor("#9DA6B5"))
            }
        }

        binding.btnContinue.setOnClickListener {
            if (binding.newPassword.text.toString() == binding.currentPassword.text.toString()) {
                Toast.makeText(context, "Password must be different with current!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (binding.newPassword.text.length < 8) {
                Toast.makeText(context, "Password must be at least 8 character!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                val response = viewModel.changePassword(
                    ChangePasswordRequest(
                        old_password = binding.currentPassword.text.toString(),
                        new_password = binding.newPassword.text.toString())
                )

                response.observe(viewLifecycleOwner) {
                    when (it.state) {
                        State.LOADING -> {
                            loadingDialog.start("Processing your password")
                        }
                        State.SUCCESS -> {
                            if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                                Toast.makeText(context, "Password changed", Toast.LENGTH_SHORT)
                                    .show()

                                with(preferences.edit()) {
                                    putBoolean(KEY_LOGGED_IN, false)
                                    apply()
                                }

                                loadingDialog.stop()
                                val intent = Intent(activity, SplashScreenActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            } else {
                                loadingDialog.stop()
                                Toast.makeText(context, it.resource?.messages, Toast.LENGTH_SHORT)
                                    .show()
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

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
    }
}