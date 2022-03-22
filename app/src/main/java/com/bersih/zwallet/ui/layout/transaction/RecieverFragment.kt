package com.bersih.zwallet.ui.layout.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentRecieverBinding
import com.bersih.zwallet.adapter.GetContactAdapter
import com.bersih.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class RecieverFragment : Fragment() {
    private lateinit var getContactAdapter: GetContactAdapter
    private lateinit var binding: FragmentRecieverBinding
    private val viewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecieverBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareData()

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
    }

    private fun prepareData() {
        this.getContactAdapter = GetContactAdapter(listOf()) { getContact, view ->
            viewModel.setSelectedContact(getContact)
            Navigation.findNavController(view).navigate(R.id.action_recieverFragment_to_inputAmountFragment)
        }

        binding.recyclerContact.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = getContactAdapter
        }

        viewModel.getContact().observe(viewLifecycleOwner) {
            when (it.state) {
                State.SUCCESS -> {
                    binding.apply {
                        loadingIndicator.visibility = View.GONE
                        recyclerContact.visibility = View.VISIBLE
                    }
                    if (it.resource?.status == HttpsURLConnection.HTTP_OK){
                        this.getContactAdapter.apply {
                            addData(it.resource.data!!)
                            binding.numberOfContact.setText(" Contacts Found " + itemCount)
                            notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(context, it.resource?.messages, Toast.LENGTH_SHORT).show()
                    }
                }
                State.LOADING -> {
                    binding.apply {
                        loadingIndicator.visibility = View.VISIBLE
                        recyclerContact.visibility = View.GONE
                    }
                }
                State.ERROR -> {
                    binding.apply {
                        loadingIndicator.visibility = View.VISIBLE
                        recyclerContact.visibility = View.GONE
                    }
                    Toast.makeText(context, it.resource?.messages, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}