package com.nifty.cloud.mb.kotlinformapp2.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.nifty.cloud.mb.kotlinformapp2.R

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvTitle: TextView
    var tvCreateDate: TextView
    var tvContents: TextView

    init {
        tvTitle = itemView.findViewById<View>(R.id.title) as TextView
        tvCreateDate = itemView.findViewById<View>(R.id.create_date) as TextView
        tvContents = itemView.findViewById<View>(R.id.contents) as TextView
    }
}
