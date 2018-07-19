package com.nifty.cloud.mb.kotlinformapp2

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView

import com.nifty.cloud.mb.kotlinformapp2.adapter.InquiryListAdapter
import com.nifty.cloud.mb.kotlinformapp2.mbaas.Mbaas
import com.nifty.cloud.mb.kotlinformapp2.model.Inquiry
import com.nifty.cloud.mb.kotlinformapp2.utils.ProgressBarFragment
import com.nifty.cloud.mb.kotlinformapp2.utils.Utils

import java.util.ArrayList

class Demo31Fragment : Fragment() {
    private var mView: View? = null
    private var inquiryList: MutableList<Inquiry> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: InquiryListAdapter? = null
    private var mProgressBar: ProgressBarFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_demo31, container, false)

            recyclerView = mView!!.findViewById<View>(R.id.recycler_view) as RecyclerView
            mAdapter = InquiryListAdapter(inquiryList)
            val mLayoutManager = LinearLayoutManager(context)
            recyclerView!!.layoutManager = mLayoutManager
            recyclerView!!.itemAnimator = DefaultItemAnimator()
            recyclerView!!.adapter = mAdapter

            val tvResultCount = mView!!.findViewById<View>(R.id.tvResultCount) as TextView
            val spnPrefecture = mView!!.findViewById<View>(R.id.spinner_prefecture) as Spinner
            val prefectureAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.prefecture_data))
            spnPrefecture.adapter = prefectureAdapter
            val inputMail = mView!!.findViewById<View>(R.id.inputMailAddress) as EditText
            val btnSearchByEmail = mView!!.findViewById<View>(R.id.btnSearchMail) as Button
            val btnSearchByPref = mView!!.findViewById<View>(R.id.btnSearchPrefecture) as Button

            btnSearchByEmail.setOnClickListener {
                tvResultCount.visibility = View.INVISIBLE
                inquiryList = ArrayList()
                mAdapter!!.setInquiryList(inquiryList)
                mAdapter!!.notifyDataSetChanged()
                dismissKeyboard(activity)

                val email = inputMail.text.toString()
                if ("" == email) {
                    Utils.showDialog(this.context!!, getString(R.string.please_fill_in_the_value))
                } else {
                    // show progress
                    mProgressBar = ProgressBarFragment()
                    fragmentManager!!.beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss()

                    Mbaas.getSearchData(Mbaas.EMAIL, email) { list, e ->
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
                            mAdapter!!.notifyDataSetChanged()
                            tvResultCount.setText(String.format(getString(R.string.condition_search_result), list.size))
                            tvResultCount.visibility = View.VISIBLE
                        }
                    }
                }
            }

            btnSearchByPref.setOnClickListener {
                tvResultCount.visibility = View.INVISIBLE
                inquiryList = ArrayList()
                mAdapter!!.setInquiryList(inquiryList)
                mAdapter!!.notifyDataSetChanged()
                dismissKeyboard(activity)

                if (spnPrefecture.selectedItemPosition == 0) {
                    Utils.showDialog(this.context!!, getString(R.string.please_fill_in_the_value))
                } else {
                    // show progress
                    mProgressBar = ProgressBarFragment()
                    fragmentManager!!.beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss()

                    Mbaas.getSearchData(Mbaas.PREFECTURE, spnPrefecture.selectedItem.toString()) { list, e ->
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
                            mAdapter!!.notifyDataSetChanged()
                            tvResultCount.setText(String.format(getString(R.string.condition_search_result), list.size))
                            tvResultCount.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        return mView
    }

    private fun dismissKeyboard(activity: Activity?) {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus)
            imm.hideSoftInputFromWindow(activity.currentFocus!!
                    .applicationWindowToken, 0)
    }
}
