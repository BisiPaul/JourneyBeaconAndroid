package com.fluffydevs.journeybeacon.common

import android.view.View
import androidx.databinding.BindingAdapter

/**
 *
 *  This class will prevent multiple clicks being dispatched.
 */
class OneClickListener(private val onClickListener: View.OnClickListener) : View.OnClickListener {
    private var lastTime: Long = 0

    override fun onClick(v: View?) {
        val current = System.currentTimeMillis()
        if ((current - lastTime) > 500) {
            onClickListener.onClick(v)
            lastTime = current
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("oneClick")
        fun setOnClickListener(view: View, f: View.OnClickListener?) {
            when (f) {
                null -> view.setOnClickListener(null)
                else -> view.setOnClickListener(OneClickListener(f))
            }
        }
    }
}