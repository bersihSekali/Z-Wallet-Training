package com.bersih.zwallet.ui.auth.pin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentCreatePinBinding
import com.bersih.zwallet.model.request.SetPinRequest
import com.bersih.zwallet.ui.auth.login.LoginViewModel
import com.bersih.zwallet.ui.main.profile.MainActivity
import com.bersih.zwallet.ui.viewModelsFactory
import com.bersih.zwallet.utils.*
import com.bersih.zwallet.widget.LoadingDialog
import javax.net.ssl.HttpsURLConnection

class CreatePinFragment : Fragment() {
    private lateinit var binding: FragmentCreatePinBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: PinViewModel by viewModelsFactory { PinViewModel(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadingDialog = LoadingDialog(requireActivity())
        binding = FragmentCreatePinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            val request = SetPinRequest(binding.pin1.text.toString() + binding.pin2.text.toString()
                            + binding.pin3.text.toString() + binding.pin4.text.toString() + binding.pin5.text.toString()
                            + binding.pin6.text.toString())

            viewModel.setPin(request).observe(viewLifecycleOwner){
                when (it.state){
                    State.LOADING -> {
                        loadingDialog.start("Processing your request ;)")
                    }
                    State.SUCCESS -> {
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            loadingDialog.stop()
                            Navigation.findNavController(view).popBackStack()
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