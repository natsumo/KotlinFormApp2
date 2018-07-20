package com.nifty.cloud.mb.kotlinformapp2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nifty.cloud.mb.kotlinformapp2.adapter.InquiryListAdapter
import com.nifty.cloud.mb.kotlinformapp2.mbaas.Mbaas
import com.nifty.cloud.mb.kotlinformapp2.model.Inquiry
import com.nifty.cloud.mb.kotlinformapp2.utils.ProgressBarFragment
import com.nifty.cloud.mb.kotlinformapp2.utils.Utils
import kotlinx.android.synthetic.main.fragment_demo2.*

import java.util.ArrayList

class Demo2Fragment : Fragment() {
    private val inquiryList = ArrayList<Inquiry>()
    private var mAdapter: InquiryListAdapter? = null
    private var mProgressBar: ProgressBarFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_demo2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the keyboard if it dispaly when initial
        Utils.dismissKeyboard(activity)

        mProgressBar = ProgressBarFragment()
        fragmentManager!!.beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss()

        mAdapter = InquiryListAdapter(inquiryList)
        val mLayoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = mAdapter
        Mbaas.getAllData { list, e ->
            if (mProgressBar != null && mProgressBar!!.isAdded) {
                fragmentManager!!.beginTransaction().remove(mProgressBar).commitAllowingStateLoss()
            }

            if (e != null) {
                //検索失敗時の処理
                Utils.showDialog(this.context!!, getString(R.string.data_acquisition_failed))
            } else {
                //検索成功時の処理
                for (obj in list!!) {
                    val inquiry = Inquiry()
                    inquiry.name = obj.getString("name").trim()
                    inquiry.title = obj.getString("title").trim()
                    inquiry.contents = obj.getString("contents").trim()
                    inquiry.age = obj.getInt("age")
                    inquiry.prefecture = obj.getString("prefecture").trim()
                    inquiry.emailAddress = obj.getString("emailAddress").trim()
                    inquiry.createDate = Utils.formatTime(obj.getString("createDate").trim())
                    inquiryList.add(inquiry)
                }
                tvResultCount.setText(String.format(getString(R.string.all_search_results), list.size))
                tvResultCount.visibility = View.VISIBLE
                mAdapter!!.notifyDataSetChanged()
            }
        }
    }
}
