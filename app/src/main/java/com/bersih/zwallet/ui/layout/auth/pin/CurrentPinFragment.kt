package com.bersih.zwallet.ui.layout.auth.pin

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentCurrentPinBinding
import com.bersih.zwallet.ui.widget.LoadingDialog
import com.bersih.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class CurrentPinFragment : Fragment() {
    private lateinit var binding: FragmentCurrentPinBinding
    private val viewModel: PinViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var keyPin: String
    var pin  = mutableListOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentPinBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextOtp()
        deleteText()

        binding.pin6.addTextChangedListener {
            if (binding.pin6.text.length > 0) {
                binding.btnContinue.setBackgroundResource(R.drawable.background_button_auth_active)
                binding.btnContinue.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (binding.pin6.text.isNullOrEmpty() || binding.pin5.text.isNullOrEmpty() ||
                    binding.pin4.text.isNullOrEmpty() || binding.pin3.text.isNullOrEmpty() ||
                    binding.pin2.text.isNullOrEmpty() || binding.pin1.text.isNullOrEmpty()) {
                binding.btnContinue.setBackgroundResource(R.drawable.background_button_auth)
                binding.btnContinue.setTextColor(Color.parseColor("#9DA6B5"))
            }
        }

        binding.btnContinue.setOnClickListener {
            if (binding.pin6.text.length < 1){
                Toast.makeText(context, "Enter your current PIN!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                prepareData(view)
            }
        }

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
    }

    fun prepareData(view: View) {
        keyPin = binding.pin1.text.toString() + binding.pin2.text.toString() +
                binding.pin3.text.toString() + binding.pin4.text.toString() +
                binding.pin5.text.toString() + binding.pin6.text.toString()

        viewModel.checkPin(keyPin).observe(viewLifecycleOwner) {
            when (it.state) {
                State.LOADING -> {
                    loadingDialog.start("Checking your current PIN")
                }
                State.SUCCESS -> {
                    if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                        loadingDialog.stop()
                        Navigation.findNavController(view).navigate(R.id.action_currentPinFragment_to_changePinFragment)
                    } else {
                        loadingDialog.stop()
                        Toast.makeText(context, "Invalid current PIN!", Toast.LENGTH_SHORT)
                    }
                }
                State.ERROR -> {
                    loadingDialog.stop()
                    Toast.makeText(context, "Invalid current PIN!", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    fun editTextOtp() {
        pin.add(0, binding.pin1)
        pin.add(1, binding.pin2)
        pin.add(2, binding.pin3)
        pin.add(3, binding.pin4)
        pin.add(4, binding.pin5)
        pin.add(5, binding.pin6)
        otpHandler()
    }

    fun otpHandler() {
        for(i in 0..5) {
            pin.get(i).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    pin.get(i).setBackgroundResource(R.drawable.background_edit_text_after)
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (i ==5 && !pin.get(i).getText().toString().isEmpty()) {
                        pin.get(i).clearFocus()
                    } else if (!pin.get(i).getText().toString().isEmpty()) {
                        pin.get(i + 1).requestFocus()
                    }
                }
            })

//            pin.get(i).setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
//                if (keyEvent.action !== KeyEvent.ACTION_DOWN) {
//                    return@OnKeyListener false
//                }
//                if (i == KeyEvent.KEYCODE_DEL && pin.get(i).getText().toString().isEmpty() && i != 0) {
//                    pin.get(i - 1).setText("")
//                    pin.get(i - 1).requestFocus()
//                    pin.get(i - 1).setBackgroundResource(R.drawable.background_edit_text)
//                }
//                false
//            })
        }
    }

    private fun deleteText() {
        binding.pin6.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.pin5.requestFocus()
                binding.pin6.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.pin5.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.pin4.requestFocus()
                binding.pin5.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.pin4.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.pin3.requestFocus()
                binding.pin4.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.pin3.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.pin2.requestFocus()
                binding.pin3.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.pin2.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.pin1.requestFocus()
                binding.pin2.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
        binding.pin1.doOnTextChanged { text, start, before, count ->
            if (count < 1) {
                binding.pin1.setBackgroundResource(R.drawable.background_edit_text)
            }
        }
    }
}