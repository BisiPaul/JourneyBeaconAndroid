package com.fluffydevs.journeybeacon.common.structure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.databinding.library.baseAdapters.BR
import androidx.navigation.fragment.findNavController
import com.fluffydevs.journeybeacon.common.utils.Extensions.autoCleared

abstract class BaseFragment<VM : BaseViewModel, B : ViewDataBinding> : Fragment() {
    abstract val layoutId: Int
    abstract val viewModel: VM
    private var _binding: B by autoCleared()

    val binding: B
        get() = _binding

    open fun allowBackButton() = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = fragmentCreateAndSetupBinding(
            viewModel, container, inflater, layoutId,
            createDataBindingComponent()
        )

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateBack.observe(viewLifecycleOwner, EventObserver<Unit> {
            findNavController().popBackStack()
        })
    }

    open fun createDataBindingComponent(): DataBindingComponent? {
        return null
    }
}

fun <VM : BaseViewModel, B : ViewDataBinding> Fragment.fragmentCreateAndSetupBinding(
    viewModel: VM,
    container: ViewGroup?,
    layoutInflater: LayoutInflater,
    layoutId: Int,
    dataBindingComponent: DataBindingComponent? = null
): B {
    val binding: B = DataBindingUtil.inflate(
        layoutInflater,
        layoutId,
        container,
        false,
        dataBindingComponent
    )

    binding.setVariable(BR.viewModel, viewModel)

    binding.lifecycleOwner = this
    binding.executePendingBindings()

    return binding
}