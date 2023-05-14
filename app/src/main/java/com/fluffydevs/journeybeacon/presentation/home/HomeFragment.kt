package com.fluffydevs.journeybeacon.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.structure.BaseFragment
import com.fluffydevs.journeybeacon.common.structure.EventObserver
import com.fluffydevs.journeybeacon.common.utils.Extensions.shortToast
import com.fluffydevs.journeybeacon.databinding.FragmentHomeBinding
import com.fluffydevs.journeybeacon.presentation.main.MainActivity
import com.fluffydevs.journeybeacon.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val layoutId: Int = R.layout.fragment_home

    override val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setControls()
        observe()
        (activity as MainActivity).apply {
            requestStartingPermissions()
            verifyBluetooth()
            startMonitoringBeacons()
        }
        observeMainViewModel()
    }

    private fun setControls() {
        binding.payButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.payTicket(
                    (activity as MainActivity).viewModel.account,
                    "4",
                    "E2"
                )
            }
        }
    }

    private fun observe() = with(viewModel) {
        paymentSuccess.observe(viewLifecycleOwner, EventObserver {
            shortToast("You payed 4 RON for E2")
        })

        paymentFailure.observe(viewLifecycleOwner, EventObserver {
            shortToast("Something went wrong. Please try again.")
        })
    }

    private fun observeMainViewModel() = with ((activity as MainActivity).viewModel) {
        hasEnteredRegion.observe(viewLifecycleOwner, EventObserver {
            binding.statusText.text = "You are inside the public transport"
            binding.payButton.isEnabled = true
        })

        hasExitedRegion.observe(viewLifecycleOwner, EventObserver {
            binding.statusText.text = "You are not inside the public transport"
            binding.payButton.isEnabled = false
        })
    }
}