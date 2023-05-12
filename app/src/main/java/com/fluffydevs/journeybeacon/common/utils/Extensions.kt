package com.fluffydevs.journeybeacon.common.utils

import androidx.fragment.app.Fragment
import com.fluffydevs.journeybeacon.common.structure.AutoClearedValue

object Extensions {
    /**
     * Creates an [AutoClearedValue] associated with this fragment.
     */
    fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)
}