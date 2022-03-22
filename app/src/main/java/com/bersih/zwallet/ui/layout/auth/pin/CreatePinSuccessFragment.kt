package com.bersih.zwallet.ui.layout.auth.pin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bersih.zwallet.databinding.FragmentCreatePinSuccessBinding
import com.bersih.zwallet.ui.layout.SplashScreenActivity
import com.bersih.zwallet.ui.layout.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePinSuccessFragment : Fragment() {
    private lateinit var binding: FragmentCreatePinSuccessBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePinSuccessBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(activity, SplashScreenActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        Handler().postDelayed({
            val intent = Intent(activity, SplashScreenActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }, 5000)
    }
}