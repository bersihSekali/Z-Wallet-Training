package com.bersih.zwallet.ui.auth.pin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentCreatePinBinding

class CreatePinFragment : Fragment() {
    private lateinit var binding: FragmentCreatePinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePinBinding.inflate(layoutInflater)
        return binding.root
    }
}