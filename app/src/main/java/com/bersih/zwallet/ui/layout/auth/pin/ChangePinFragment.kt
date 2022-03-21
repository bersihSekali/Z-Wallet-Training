package com.bersih.zwallet.ui.layout.auth.pin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentChangePinBinding
import com.bersih.zwallet.model.request.SetPinRequest
import com.bersih.zwallet.ui.layout.main.MainActivity
import com.bersih.zwallet.ui.widget.LoadingDialog
import com.bersih.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class ChangePinFragment : Fragment() {
    private lateinit var binding: FragmentChangePinBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: PinViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePinBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        binding.btnContinue.setOnClickListener {
            val request = SetPinRequest(binding.pin1.text.toString() + binding.pin2.text.toString()
                    + binding.pin3.text.toString() + binding.pin4.text.toString() + binding.pin5.text.toString()
                    + binding.pin6.text.toString())

            viewModel.setPin(request).observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Processing your request ;)")
                    }
                    State.SUCCESS -> {
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            loadingDialog.stop()
                            Navigation.findNavController(view).navigate(R.id.action_changePinFragment_to_createPinSuccessFragment)
                        } else {
                            Toast.makeText(context, it.resource?.messages, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    State.ERROR -> {
                        loadingDialog.stop()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}