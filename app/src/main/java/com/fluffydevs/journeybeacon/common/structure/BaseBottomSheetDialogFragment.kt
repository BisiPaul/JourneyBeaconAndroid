package com.fluffydevs.journeybeacon.common.structure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.fluffydevs.journeybeacon.common.utils.Extensions.autoCleared
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 *  Created by paulbisioc on 16.02.2023
 */
abstract class BaseBottomSheetDialogFragment<VM : BaseViewModel, B : ViewDataBinding> :
    BottomSheetDialogFragment() {

    abstract val layoutId: Int
    abstract val viewModel: VM
    private var _binding: B by autoCleared()

    val binding: B
        get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = fragmentCreateAndSetupBinding(viewModel, container, inflater, layoutId)

        return _binding.root
    }
}