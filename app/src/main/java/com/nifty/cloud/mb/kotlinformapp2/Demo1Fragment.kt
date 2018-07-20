package com.nifty.cloud.mb.kotlinformapp2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.nifty.cloud.mb.kotlinformapp2.mbaas.Mbaas
import com.nifty.cloud.mb.kotlinformapp2.utils.ProgressBarFragment
import com.nifty.cloud.mb.kotlinformapp2.utils.Utils
import kotlinx.android.synthetic.main.fragment_demo1.*

import java.util.ArrayList


class Demo1Fragment : Fragment() {
    private var mProgressBar: ProgressBarFragment? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_demo1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utils.setupUI(parent, activity)

        // Hide the keyboard if it dispaly when initial
        Utils.dismissKeyboard(activity)

        val ages = ArrayList<String>()
        ages.add("- age -")
        for (i in 0..120) {
            ages.add(i.toString())
        }
        val ageAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, ages)
        spinner_age.adapter = ageAdapter

        val prefectureAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.prefecture_data))
        spinner_prefecture.adapter = prefectureAdapter

        btnSubmit.setOnClickListener {

            //Hide the keyboard
            Utils.dismissKeyboard(activity)

            if (inputName.text.toString().isNullOrBlank()) {
                Utils.showDialog(context!!, getString(R.string.name_is_not_entered))
            } else if (inputMailAddress.text.toString().isNullOrBlank()) {
                Utils.showDialog(context!!, getString(R.string.email_is_not_entered))
            } else if (spinner_age.selectedItemPosition == 0) {
                Utils.showDialog(context!!, getString(R.string.age_has_not_been_entered))
            } else if (spinner_prefecture.selectedItemPosition == 0) {
                Utils.showDialog(context!!, getString(R.string.province_is_not_entered))
            } else if (inputTitle.text.toString().isNullOrBlank()) {
                Utils.showDialog(context!!, getString(R.string.inquiry_title_has_not_been_entered))
            } else if (inputContents.text.toString().isNullOrBlank()) {
                Utils.showDialog(context!!, getString(R.string.inquiry_content_is_not_entered))
            } else {
                // show progress
                mProgressBar = ProgressBarFragment()
                fragmentManager!!.beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss()

                val name = inputName.text.toString()
                val email = inputMailAddress.text.toString()
                val age = spinner_age.selectedItemPosition - 1
                val prefecture = spinner_prefecture.selectedItem.toString()
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

}
