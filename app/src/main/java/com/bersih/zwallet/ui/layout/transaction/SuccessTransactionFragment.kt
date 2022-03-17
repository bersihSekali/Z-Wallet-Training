package com.bersih.zwallet.ui.layout.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentSuccessTransactionBinding

class SuccessTransactionFragment : Fragment() {
    private lateinit var binding: FragmentSuccessTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccessTransactionBinding.inflate(layoutInflater)
        return binding.root
    }
}