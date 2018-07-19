package com.nifty.cloud.mb.kotlinformapp2.utils

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar

import com.nifty.cloud.mb.kotlinformapp2.R

class ProgressBarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val progressBar = ProgressBar(container!!.context)
        progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        if (container is FrameLayout) {
            val layoutParams = FrameLayout.LayoutParams(PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT, Gravity.CENTER)
            progressBar.layoutParams = layoutParams
        }

        return progressBar
    }

    companion object {
        private val PROGRESS_BAR_WIDTH = 100
        private val PROGRESS_BAR_HEIGHT = 100
    }
}

