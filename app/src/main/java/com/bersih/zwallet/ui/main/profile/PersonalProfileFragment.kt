package com.bersih.zwallet.ui.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentPersonalProfileBinding

class PersonalProfileFragment : Fragment() {
    private lateinit var binding: FragmentPersonalProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }
}