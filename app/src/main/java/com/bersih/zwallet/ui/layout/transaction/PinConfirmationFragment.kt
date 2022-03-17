package com.bersih.zwallet.ui.layout.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentPinConfirmationBinding

class PinConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentPinConfirmationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinConfirmationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_pinConfirmationFragment_to_inputAmountFragment)
        }

        binding.btnContinue.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_pinConfirmationFragment_to_failedTransactionFragment)
        }
    }
}