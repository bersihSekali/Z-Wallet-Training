package com.bersih.zwallet.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bersih.zwallet.databinding.FragmentOtpBinding

class OtpFragment : Fragment() {
    private lateinit var binding: FragmentOtpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater)
        return binding.root
    }
}