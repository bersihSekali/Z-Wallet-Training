package com.bersih.zwallet.ui.layout.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentPinConfirmationBinding
import com.bersih.zwallet.model.request.TransferRequest
import com.bersih.zwallet.ui.widget.LoadingDialog
import com.bersih.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentPinConfirmationBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinConfirmationBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareData(view)

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_pinConfirmationFragment_to_inputAmountFragment)
        }
    }

    private fun prepareData(view: View) {
        var amount: String ?= null
        var notes: String ?= null
        var receiver: String ?= null
        var request: TransferRequest?= null

        binding.btnContinue.setOnClickListener {
            val pin = binding.pin1.text.toString() + binding.pin2.text.toString() +
                    binding.pin3.text.toString() + binding.pin4.text.toString() + binding.pin5.text.toString() +
                    binding.pin6.text.toString()

            viewModel.getTransferParam().observe(viewLifecycleOwner) {
                request = it
                binding.apply {
                    receiver = it.receiver
                    amount = it.amount.toString()
                    notes = it.notes
                }
            }

            viewModel.transfer(request!!, pin).observe(viewLifecycleOwner) {
                when(it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Transfer being process")
                    }
                    State.SUCCESS -> {
                        loadingDialog.start("Successully Transfered")
                        loadingDialog.stop()
                        Navigation.findNavController(view).navigate(R.id.action_pinConfirmationFragment_to_successTransactionFragment)
                    }
                    State.ERROR -> {
                        loadingDialog.stop()
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(view).navigate(R.id.action_pinConfirmationFragment_to_failedTransactionFragment)
                    }
                }
            }
        }
    }
}