package com.fluffydevs.journeybeacon.common.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fluffydevs.journeybeacon.common.structure.AutoClearedValue

object Extensions {
    // Toast
    fun Context.shortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.shortToast(message: String) {
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun Context.longToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Creates an [AutoClearedValue] associated with this fragment.
     */
    fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)
}