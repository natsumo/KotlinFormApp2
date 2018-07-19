package com.nifty.cloud.mb.kotlinformapp2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.nifty.cloud.mb.kotlinformapp2.adapter.InquiryListAdapter
import com.nifty.cloud.mb.kotlinformapp2.mbaas.Mbaas
import com.nifty.cloud.mb.kotlinformapp2.model.Inquiry
import com.nifty.cloud.mb.kotlinformapp2.utils.ProgressBarFragment
import com.nifty.cloud.mb.kotlinformapp2.utils.Utils
import kotlinx.android.synthetic.main.fragment_demo32.*

import java.util.ArrayList

class Demo32Fragment : Fragment() {
    private var inquiryList: MutableList<Inquiry> = ArrayList()
    private var mAdapter: InquiryListAdapter? = null
    private var mProgressBar: ProgressBarFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_demo32, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the keyboard if it dispaly when initial
        Utils.dismissKeyboard(activity)

        mAdapter = InquiryListAdapter(inquiryList)
        val mLayoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = mAdapter

        val spinnerAgeGreaterThan = spinnerAgeGreaterThan
        val spinnerAgeLessThan = spinnerAgeLessThan

        val ages = ArrayList<String>()
        ages.add("- age -")
        for (i in 0..120) {
            ages.add(i.toString())
        }
        val ageAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, ages)

        spinnerAgeGreaterThan.adapter = ageAdapter
        spinnerAgeLessThan.adapter = ageAdapter

        btnSearch.setOnClickListener {
            inquiryList = ArrayList()
            mAdapter!!.setInquiryList(inquiryList)
            mAdapter!!.notifyDataSetChanged()
            tvResultCount.visibility = View.INVISIBLE

            var ageGreaterThan = spinnerAgeGreaterThan.selectedItemPosition
            var ageLessThan: Int? = spinnerAgeLessThan.selectedItemPosition

            if (ageGreaterThan == 0 || ageLessThan == 0) {
                Utils.showDialog(this.context!!, getString(R.string.please_fill_in_the_value))
            } else if (ageGreaterThan >= ageLessThan!!) {
                Utils.showDialog(this.context!!, getString(R.string.value_is_invalid))
            } else {
                // show progress
                mProgressBar = ProgressBarFragment()
                fragmentManager!!.beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss()

                ageGreaterThan -= 1
                ageLessThan -= 1
                Mbaas.getRangeSearchData(ageGreaterThan, ageLessThan) { list, e ->
                    // remove progress
                    if (mProgressBar != null && mProgressBar!!.isAdded) {
                        fragmentManager!!.beginTransaction().remove(mProgressBar).commitAllowingStateLoss()
                    }

                    if (e != null) {
                        Utils.showDialog(this.context!!, getString(R.string.data_acquisition_failed))
                    } else {
                        //検索成功時の処理
                        for (obj in list!!) {
                            val inquiry = Inquiry()
                            inquiry.name = obj.getString("name")
                            inquiry.title = obj.getString("title")
                            inquiry.contents = obj.getString("contents")
                            inquiry.age = obj.getInt("age")
                            inquiry.prefecture = obj.getString("prefecture")
                            inquiry.emailAddress = obj.getString("emailAddress")
                            inquiry.createDate = Utils.formatTime(obj.getString("createDate"))
                            inquiryList.add(inquiry)
                        }
                        mAdapter!!.setInquiryList(inquiryList)
                        mAdapter!!.notifyDataSetChanged()
                        tvResultCount.setText(String.format(getString(R.string.conditional_search_range_result), list.size))
                        tvResultCount.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
