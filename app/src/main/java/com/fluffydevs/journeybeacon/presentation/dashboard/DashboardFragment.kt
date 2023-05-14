package com.fluffydevs.journeybeacon.presentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.structure.BaseFragment
import com.fluffydevs.journeybeacon.common.structure.EventObserver
import com.fluffydevs.journeybeacon.common.utils.Extensions.shortToast
import com.fluffydevs.journeybeacon.databinding.FragmentDashboardBinding
import com.fluffydevs.journeybeacon.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : BaseFragment<DashboardViewModel, FragmentDashboardBinding>() {

    override val layoutId: Int = R.layout.fragment_dashboard

    override val viewModel by viewModels<DashboardViewModel>()

    private lateinit var paymentsAdapter: PaymentsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setControls()
        observe()
    }

    override fun onResume() {
        super.onResume()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPayments((activity as MainActivity).viewModel.account)
        }
    }

    private fun setControls() {

    }

    private fun observe() = with(viewModel) {
        viewModel.paymentsLiveData.observe(viewLifecycleOwner) {
            paymentsAdapter.submitList(it)
        }

        fetchPaymentsSuccess.observe(viewLifecycleOwner, EventObserver {
            shortToast("Transactions fetched successfuly")
        })

        fetchPaymentsFailure.observe(viewLifecycleOwner, EventObserver {
            shortToast("Something went wrong, please try again.")
        })
    }

    private fun setupRecyclerView() {
        paymentsAdapter = PaymentsAdapter()

        binding.paymentsList.apply {
            adapter = paymentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}