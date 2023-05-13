package com.fluffydevs.journeybeacon.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.structure.BaseFragment
import com.fluffydevs.journeybeacon.common.structure.EventObserver
import com.fluffydevs.journeybeacon.databinding.FragmentSplashBinding
import com.fluffydevs.journeybeacon.presentation.main.MainActivity

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    override val layoutId = R.layout.fragment_splash

    override val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setControls()
        observe()
        observeMainViewModel()
    }

    private fun setControls() {

    }

    private fun observe() = with(viewModel) {

    }

    private fun observeMainViewModel() = with((activity as MainActivity).viewModel) {
        userAlreadySignedIn.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        })

        newUser.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        })
    }
}