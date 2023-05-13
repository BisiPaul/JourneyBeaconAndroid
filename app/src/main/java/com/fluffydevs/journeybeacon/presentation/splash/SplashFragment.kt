package com.fluffydevs.journeybeacon.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.structure.BaseFragment
import com.fluffydevs.journeybeacon.common.structure.EventObserver
import com.fluffydevs.journeybeacon.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    override val layoutId = R.layout.fragment_splash

    override val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setControls()
        observe()
    }

    private fun setControls() {

    }

    private fun observe() = with(viewModel) {
        navigateToLogin.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        })
    }
}