package com.bersih.zwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bersih.zwallet.R
import com.bersih.zwallet.databinding.ListContactBinding
import com.bersih.zwallet.model.request.GetContact
import com.bersih.zwallet.utils.BASE_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class GetContactAdapter(
    private var data: List<GetContact>,
    private val clickListener: (GetContact, View) -> Unit,
): RecyclerView.Adapter<GetContactAdapter.GetContactAdapterHolder>() {
    private lateinit var contextAdapter: Context

    class GetContactAdapterHolder(private val binding: ListContactBinding): RecyclerView
    .ViewHolder(binding.root) {
        fun bindData(data: GetContact, onClick: (GetContact, View) -> Unit){
            binding.textContactName.text = data.name
            binding.textContactPhoneNumber.text = data.phone
            Glide.with(binding.imageContact)
                .load(BASE_URL + data.image)
                .apply(
                    RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.ic_baseline_broken_image_24)
                )
                .into(binding.imageContact)

            binding.root.setOnClickListener { onClick(data, binding.root) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetContactAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        this.contextAdapter = parent.context
        val binding = ListContactBinding.inflate(inflater, parent, false)
        return GetContactAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: GetContactAdapterHolder, position: Int) {
        holder.bindData(this.data[position], clickListener)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun addData(data: List<GetContact>) {
        this.data = data
    }
}
