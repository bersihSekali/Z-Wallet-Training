package com.bersih.zwallet.ui.main.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bersih.zwallet.R
import com.bersih.zwallet.adapter.TransactionAdapter
import com.bersih.zwallet.databinding.FragmentPersonalProfileBinding
import com.bersih.zwallet.ui.main.home.HomeViewModel
import com.bersih.zwallet.ui.viewModelsFactory
import com.bersih.zwallet.utils.*
import com.bersih.zwallet.utils.Helper.formatPrice
import com.bersih.zwallet.widget.LoadingDialog
import javax.net.ssl.HttpsURLConnection

class PersonalProfileFragment : Fragment() {
    private lateinit var binding: FragmentPersonalProfileBinding
    private lateinit var preferences : SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalProfileBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()
    }

    private fun prepareData() {
        viewModel.getMyProfile().observe(viewLifecycleOwner){
            when (it.state) {
                State.LOADING -> {
                    loadingDialog.start("Processing your request ;)")
                }
                State.SUCCESS -> {
                    if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                        binding.apply {
                            firstName.text = it.resource.data?.firstname
                            lastName.text = it.resource.data?.lastname
                            phoneNumber.text = it.resource.data?.phone
                        }
                    } else {
                        Toast.makeText(context, it.resource?.messages, Toast.LENGTH_SHORT).show()
                    }
                    loadingDialog.stop()
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