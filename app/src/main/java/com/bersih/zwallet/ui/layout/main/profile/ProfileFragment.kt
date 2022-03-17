package com.bersih.zwallet.ui.layout.main.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentProfileBinding
import com.bersih.zwallet.ui.layout.SplashScreenActivity
import com.bersih.zwallet.ui.layout.main.home.HomeViewModel
import com.bersih.zwallet.utils.KEY_LOGGED_IN
import com.bersih.zwallet.utils.PREFS_NAME
import com.bersih.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var preferences : SharedPreferences
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        preferences = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_homeFragment)
        }

        viewModel.getMyProfile().observe(viewLifecycleOwner){
            when (it.state) {
                State.LOADING -> {

                }
                State.SUCCESS -> {
                    if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                        binding.apply {
                            textUsername.text = it.resource.data?.firstname + " " + it.resource.data?.lastname
                            textPhone.text = it.resource.data?.phone
                        }
                    } else {
                        Toast.makeText(context, it.resource?.messages, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {

                }
            }
        }

        binding.btnChangePassword.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Logout Confirmation")
                .setMessage("Are you sure wanna logout?")
                .setPositiveButton("Yes") {_, _ ->
                    with(preferences.edit()) {
                        putBoolean(KEY_LOGGED_IN, false)
                        apply()
                    }

                    val intent = Intent(activity, SplashScreenActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") {_, _ ->
                    return@setNegativeButton
                }
                .show()
        }

        binding.btnPersonalInformation.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_personalProfileFragment)
        }

        binding.btnChangePin.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_currentPinFragment)
        }
    }
}