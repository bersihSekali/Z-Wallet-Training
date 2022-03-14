package com.bersih.zwallet.ui.main.home

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
import com.bersih.zwallet.databinding.FragmentHomeBinding
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.GetUserDetail
import com.bersih.zwallet.network.NetworkConfig
import com.bersih.zwallet.ui.main.profile.MainActivity
import com.bersih.zwallet.ui.viewModelsFactory
import com.bersih.zwallet.utils.*
import com.bersih.zwallet.utils.Helper.formatPrice
import com.bersih.zwallet.widget.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class HomeFragment : Fragment() {
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var preferences : SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()

        binding.imageProfile.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
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
                        loadingIndicator.visibility = View.VISIBLE
                        recyclerTransaction.visibility = View.GONE
                    }
                }
                State.SUCCESS -> {
                    binding.apply {
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
                        loadingIndicator.visibility = View.GONE
                        recyclerTransaction.visibility = View.VISIBLE
                    }
                    Toast.makeText(context, it.resource?.messages, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getBalance().observe(viewLifecycleOwner) {
            if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    textAmount.formatPrice(it.resource.data?.get(0)?.balance.toString())
                    textUsername.text = it.resource.data?.get(0)?.name
                    textPhone.text = it.resource.data?.get(0)?.phone
                }
            } else {
                Toast.makeText(context, it.resource?.messages, Toast.LENGTH_SHORT).show()
            }
        }
    }
}