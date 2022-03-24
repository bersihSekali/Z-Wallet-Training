package com.bersih.zwallet.ui.layout.transaction

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentFailedTransactionBinding
import com.bersih.zwallet.databinding.FragmentSuccessTransactionBinding
import com.bersih.zwallet.ui.layout.main.MainActivity
import com.bersih.zwallet.ui.layout.main.home.HomeViewModel
import com.bersih.zwallet.utils.BASE_URL
import com.bersih.zwallet.utils.Helper.formatPrice
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.net.ssl.HttpsURLConnection

class FailedTransactionFragment : Fragment() {
    private lateinit var binding: FragmentFailedTransactionBinding
    private val viewModel: TransactionViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFailedTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHome.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_failedTransactionFragment_to_homeFragment)
        }

        prepareData(view)
    }

    private fun prepareData(view: View) {
        viewModel.getTransferParam().observe(viewLifecycleOwner) {
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

        viewModel.selectedContact().observe(viewLifecycleOwner) {
            binding.textUsername.text = it.name
            binding.textPhone.text = it.phone
            Glide.with(binding.imageContact)
                .load(BASE_URL + it.image)
                .apply(
                    RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.ic_baseline_broken_image_24)
                )
                .into(binding.imageContact)
        }

        homeViewModel.getBalance().observe(viewLifecycleOwner) {
//            binding.textBalanced.text = "Rp" + it.resource?.data?.get(0)?.balance.toString()
            if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    textBalanced.formatPrice(it.resource.data?.get(0)?.balance.toString())
                }
            }
        }
    }
}