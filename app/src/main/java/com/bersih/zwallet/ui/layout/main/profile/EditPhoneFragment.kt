package com.bersih.zwallet.ui.layout.main.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentEditPhoneBinding
import com.bersih.zwallet.model.request.EditPhoneRequest
import com.bersih.zwallet.ui.layout.main.home.HomeViewModel
import com.bersih.zwallet.ui.widget.LoadingDialog
import com.bersih.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class EditPhoneFragment : Fragment() {
    private lateinit var binding: FragmentEditPhoneBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPhoneBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        prepareData(view)

        binding.inputPhoneNumber.addTextChangedListener {
            if (binding.inputPhoneNumber.text.length > 8) {
                binding.btnConfirm.setBackgroundResource(R.drawable.background_button_auth_active)
                binding.btnConfirm.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (binding.inputPhoneNumber.text.length <= 8) {
                binding.btnConfirm.setBackgroundResource(R.drawable.background_button_auth)
                binding.btnConfirm.setTextColor(Color.parseColor("#9DA6B5"))
            }
        }
    }

    fun prepareData(view: View) {
        binding.btnConfirm.setOnClickListener {
            if (binding.inputPhoneNumber.text.isNullOrEmpty()) {
                Toast.makeText(activity, "Fill the field!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = EditPhoneRequest(binding.inputPhoneNumber.text.toString())

            viewModel.editPhone(request).observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Saving your phone number")
                    }
                    State.SUCCESS -> {
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK){
                            loadingDialog.stop()
                            Toast.makeText(context, "Phone number saved", Toast.LENGTH_LONG).show()
                            Navigation.findNavController(view).navigate(R.id.editPhoneFragment)
                        } else {
                            loadingDialog.stop()
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    State.ERROR -> {
                        loadingDialog.stop()
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}