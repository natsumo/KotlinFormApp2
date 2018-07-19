package com.nifty.cloud.mb.kotlinformapp2.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.nifty.cloud.mb.kotlinformapp2.R
import com.nifty.cloud.mb.kotlinformapp2.model.Inquiry
import com.nifty.cloud.mb.kotlinformapp2.utils.Utils

class InquiryListAdapter(private var inquiryList: List<Inquiry>?) : RecyclerView.Adapter<ViewHolder>() {

    fun setInquiryList(listItem: List<Inquiry>) {
        inquiryList = listItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item, parent, false)
        val holder = ViewHolder(itemView)
        itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                Utils.showDialog(parent.context, inquiryList!![position].contents!!)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val inquiry = inquiryList!![position]
        holder.tvTitle.text = inquiry.title
        holder.tvCreateDate.text = inquiry.createDate
        holder.tvContents.text = StringBuilder(inquiry.name).append("( ").append(inquiry.prefecture)
                .append(" ) -").append(inquiry.age).append("- ").append(inquiry.emailAddress).toString()
    }

    override fun getItemCount(): Int {
        return if (inquiryList != null) {
            inquiryList!!.size
        } else 0
    }
}