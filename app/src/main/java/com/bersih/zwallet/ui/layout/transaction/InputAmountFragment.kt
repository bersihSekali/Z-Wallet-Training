package com.bersih.zwallet.ui.layout.transaction

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentInputAmountBinding
import com.bersih.zwallet.model.request.TransferRequest
import com.bersih.zwallet.ui.layout.main.home.HomeViewModel
import com.bersih.zwallet.utils.BASE_URL
import com.bersih.zwallet.utils.Helper.formatPrice
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class InputAmountFragment : Fragment() {
    private lateinit var binding: FragmentInputAmountBinding
    private val viewModel: TransactionViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputAmountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareData(view)

        binding.textAmount.addTextChangedListener {
            if (binding.textAmount.text.length > 0) {
                binding.btnContinue.setBackgroundResource(R.drawable.background_button_auth_active)
                binding.btnContinue.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_inputAmountFragment_to_homeFragment)
        }
    }

    fun prepareData(view: View) {
        var receiver: String ?= null

        viewModel.selectedContact().observe(viewLifecycleOwner) {
            receiver = it.id.toString()
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

        binding.btnContinue.setOnClickListener {
            viewModel.setTransferParam(TransferRequest(
                receiver,
                binding.textAmount.text.toString().toInt(),
                binding.textNotes.text.toString()
            ))

            Navigation.findNavController(view).navigate(R.id.action_inputAmountFragment_to_confirmationFragment)
        }
    }
}