package com.bersih.zwallet.ui.main.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bersih.zwallet.R
import com.bersih.zwallet.adapter.TransactionAdapter
import com.bersih.zwallet.data.Transaction
import com.bersih.zwallet.databinding.ActivityMainBinding
import com.bersih.zwallet.databinding.FragmentHomeBinding
import com.bersih.zwallet.model.ApiResponse
import com.bersih.zwallet.model.GetUserDetail
import com.bersih.zwallet.model.request.GetUserDetailRequest
import com.bersih.zwallet.network.NetworkConfig
import com.bersih.zwallet.utils.KEY_LOGGED_IN
import com.bersih.zwallet.utils.PREFS_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.prefs.Preferences
import javax.net.ssl.HttpsURLConnection

class HomeFragment : Fragment() {
    private val transactionData = mutableListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var prefs : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        NetworkConfig(context).getService().getUserDetail()
            .enqueue(object: Callback<ApiResponse<List<GetUserDetail>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<GetUserDetail>>>,
                response: Response<ApiResponse<List<GetUserDetail>>>
            ) {
                if (response.body()?.status != HttpsURLConnection.HTTP_OK) {
                    Toast.makeText(context, "Fetch detail data failed", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val res = response.body()?.data?.get(0)
                    binding.textUsername.text = "${res?.name}"
                    binding.textAmount.text = "${res?.balance}"
                    binding.textPhone.text = "${res?.phone}"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<GetUserDetail>>>, t: Throwable) {
                Toast.makeText(context, "Fetch detail data failed", Toast.LENGTH_SHORT)
                    .show()
            }

        })

        this.transactionAdapter = TransactionAdapter(transactionData)
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerTransaction.layoutManager = layoutManager
        binding.recyclerTransaction.adapter = transactionAdapter
        prepareData()

        binding.imageProfile.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun prepareData() {
        this.transactionData.add(Transaction(
            transactionImg = activity?.getDrawable(R.drawable.sample)!!,
            transactionName = "Grinaldi Wisnu",
            transactionNominal = 125000.00,
            transactionType = "Transfer"
        ))

        this.transactionAdapter.notifyDataSetChanged()

    }
}