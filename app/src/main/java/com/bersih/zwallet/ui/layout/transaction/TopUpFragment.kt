package com.bersih.zwallet.ui.layout.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.FragmentTopUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopUpFragment : Fragment() {
    private lateinit var binding: FragmentTopUpBinding
    private val viewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        prepareData(view)
    }

    private fun prepareData(view: View) {
        viewModel.getBalance().observe(viewLifecycleOwner) {
            binding.virtualAccount.text = it.resource?.data?.get(0)?.phone
        }
    }
}