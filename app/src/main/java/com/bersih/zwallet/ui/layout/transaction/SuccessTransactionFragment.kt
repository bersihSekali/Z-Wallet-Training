package com.bersih.zwallet.ui.layout.transaction

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentSuccessTransactionBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SuccessTransactionFragment : Fragment() {
    private lateinit var binding: FragmentSuccessTransactionBinding
    private val viewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccessTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_successTransactionFragment_to_homeFragment)
        }

        prepareData(view)
    }

    private fun prepareData(view: View) {
        viewModel.getTransferParam().observe(viewLifecycleOwner) {
            binding.textUsername.text = it.receiver
            binding.textAmount.text = it.amount.toString()
            binding.textNotes.text = it.notes

            if (it.notes.isNullOrEmpty()) {
                binding.textNotes.text = ""
            } else {
                binding.textNotes.text = it.notes
            }

            // format date
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mma")
                val answer =  current.format(formatter)
                binding.textDate.text = answer
            } else {
                val date = Date()
                val formatter = SimpleDateFormat("MMM dd, yyyy - HH:mma")
                val answer = formatter.format(date)
                binding.textDate.text = answer
            }
        }
    }
}