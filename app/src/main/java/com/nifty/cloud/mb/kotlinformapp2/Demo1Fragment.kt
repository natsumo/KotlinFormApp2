package com.nifty.cloud.mb.kotlinformapp2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

import com.nifty.cloud.mb.kotlinformapp2.mbaas.Mbaas
import com.nifty.cloud.mb.kotlinformapp2.utils.ProgressBarFragment
import com.nifty.cloud.mb.kotlinformapp2.utils.Utils

import java.util.ArrayList

class Demo1Fragment : Fragment() {
    private var mView: View? = null
    private var mProgressBar: ProgressBarFragment? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_demo1, container, false)

            val spnAge = mView!!.findViewById<View>(R.id.spinner_age) as Spinner
            val ages = ArrayList<String>()
            ages.add("- age -")
            for (i in 0..120) {
                ages.add(i.toString())
            }
            val ageAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, ages)
            spnAge.adapter = ageAdapter

            val spnPrefecture = mView!!.findViewById<View>(R.id.spinner_prefecture) as Spinner
            val prefectureAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.prefecture_data))
            spnPrefecture.adapter = prefectureAdapter

            val inputName = mView!!.findViewById<View>(R.id.inputName) as EditText
            val inputMail = mView!!.findViewById<View>(R.id.inputMailAddress) as EditText
            val inputTitle = mView!!.findViewById<View>(R.id.inputTitle) as EditText
            val inputContents = mView!!.findViewById<View>(R.id.inputContents) as EditText
            val btnSubmit = mView!!.findViewById<View>(R.id.btnSubmit) as Button
            btnSubmit.setOnClickListener {
                if ("" == inputName.text.toString()) {
                    Utils.showDialog(context!!, getString(R.string.name_is_not_entered))
                } else if ("" == inputMail.text.toString()) {
                    Utils.showDialog(context!!, getString(R.string.email_is_not_entered))
                } else if (spnAge.selectedItemPosition == 0) {
                    Utils.showDialog(context!!, getString(R.string.age_has_not_been_entered))
                } else if (spnPrefecture.selectedItemPosition == 0) {
                    Utils.showDialog(context!!, getString(R.string.province_is_not_entered))
                } else if ("" == inputTitle.text.toString()) {
                    Utils.showDialog(context!!, getString(R.string.inquiry_title_has_not_been_entered))
                } else if ("" == inputContents.text.toString()) {
                    Utils.showDialog(context!!, getString(R.string.inquiry_content_is_not_entered))
                } else {
                    // show progress
                    mProgressBar = ProgressBarFragment()
                    fragmentManager!!.beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss()

                    val name = inputName.text.toString()
                    val email = inputMail.text.toString()
                    val age = spnAge.selectedItemPosition - 1
                    val prefecture = spnPrefecture.selectedItem.toString()
                    val title = inputTitle.text.toString()
                    val contents = inputContents.text.toString()
                    Mbaas.saveData(name, email, age, prefecture, title, contents) { e ->
                        // remove progress
                        if (mProgressBar != null && mProgressBar!!.isAdded) {
                            fragmentManager!!.beginTransaction().remove(mProgressBar).commitAllowingStateLoss()
                        }
                        if (e != null) {
                            //保存失敗
                            Utils.showDialog(context!!, getString(R.string.failed_to_accept_inquiries))
                        } else {
                            // 保存成功
                            Utils.showDialog(context!!, getString(R.string.inquiries_accepted))
                        }
                    }
                }
            }
        }
        return mView

    }
}
