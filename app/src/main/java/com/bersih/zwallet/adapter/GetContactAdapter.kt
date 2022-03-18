package com.bersih.zwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bersih.zwallet.R
import com.bersih.zwallet.model.request.GetContact
import com.bersih.zwallet.utils.BASE_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

class GetContactAdapter(private var data: List<GetContact>): RecyclerView.Adapter<GetContactAdapter.GetContactAdapterHolder>() {
    lateinit var contextAdapter: Context

    class GetContactAdapterHolder(view: View): RecyclerView.ViewHolder(view) {
        private val image: ShapeableImageView = view.findViewById(R.id.imageContact)
        private val name: TextView = view.findViewById(R.id.textContactName)
        private val phoneNumber: TextView = view.findViewById(R.id.textContactPhoneNumber)

        fun bindData(data: GetContact, context: Context, position: Int) {
            name.text = data.name.toString()
            phoneNumber.text = data.phone.toString()
            Glide.with(image).load(BASE_URL + data.image)
                .apply(
                    RequestOptions.circleCropTransform().placeholder(R.drawable.ic_baseline_broken_image_24)
                )
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetContactAdapter.GetContactAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        this.contextAdapter = parent.context

        val inflatedView: View = inflater.inflate(R.layout.list_contact, parent, false)
        return GetContactAdapterHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: GetContactAdapter.GetContactAdapterHolder, position: Int) {
        holder.bindData(this.data[position], contextAdapter, position)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun addData(data: List<GetContact>){
        this.data = data
    }
}
