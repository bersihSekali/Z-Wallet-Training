package com.bersih.zwallet.ui.layout.main.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentHomeBinding
import com.bersih.zwallet.adapter.TransactionAdapter
import com.bersih.zwallet.model.GetInvoice
import com.bersih.zwallet.ui.widget.LoadingDialog
import com.bersih.zwallet.utils.BASE_URL
import com.bersih.zwallet.utils.Helper.formatPrice
import com.bersih.zwallet.utils.PREFS_NAME
import com.bersih.zwallet.utils.State
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var preferences : SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()

        binding.imageProfile.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.topUp.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_topUpFragment)
        }

        binding.transfer.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_recieverFragment)
        }
    }

    private fun prepareData() {
        this.transactionAdapter = TransactionAdapter(listOf())

        binding.recyclerTransaction.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        viewModel.getInvoice().observe(viewLifecycleOwner) {
            when (it.state) {
                State.LOADING -> {
                    binding.apply {
                        loadingDialog.start("Processing history")
                        loadingIndicator.visibility = View.VISIBLE
                        recyclerTransaction.visibility = View.GONE
                    }
                }
                State.SUCCESS -> {
                    binding.apply {
                        loadingDialog.stop()
                        loadingIndicator.visibility = View.GONE
                        recyclerTransaction.visibility = View.VISIBLE
                    }
                    if (it.resource?.status == HttpsURLConnection.HTTP_OK){
                        this.transactionAdapter.apply {
                            addData(it.resource.data!!)
                            notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(context, it.resource?.messages, Toast.LENGTH_SHORT).show()
                    }
                }
                State.ERROR -> {
                    binding.apply {
                        loadingDialog.stop()
                        loadingIndicator.visibility = View.GONE
                        recyclerTransaction.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.getBalance().observe(viewLifecycleOwner) {
            if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    textAmount.formatPrice(it.resource.data?.get(0)?.balance.toString())
                    textUsername.text = it.resource.data?.get(0)?.name
                    textPhone.text = it.resource.data?.get(0)?.phone
                    Glide.with(imageProfile)
                        .load(BASE_URL + it.resource.data?.get(0)?.image)
                        .apply(
                            RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.ic_baseline_broken_image_24)
                        )
                        .into(imageProfile)
                }
            } else {
                Toast.makeText(context, "${it.resource?.messages}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}