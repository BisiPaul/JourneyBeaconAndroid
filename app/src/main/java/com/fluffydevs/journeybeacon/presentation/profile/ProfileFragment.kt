package com.fluffydevs.journeybeacon.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.structure.BaseFragment
import com.fluffydevs.journeybeacon.common.structure.EventObserver
import com.fluffydevs.journeybeacon.databinding.FragmentProfileBinding
import com.fluffydevs.journeybeacon.presentation.main.MainActivity

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val layoutId: Int = R.layout.fragment_profile

    override val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
        setControls()
        observe()
        observeMainViewModel()
    }

    private fun setup() {
        binding.welcomeName.text = (activity as MainActivity).viewModel.account.displayName
    }

    private fun setControls() {
        binding.signOutButton.setOnClickListener {
            (activity as MainActivity).signOut()
        }
    }

    private fun observe() = with(viewModel) {

    }

    private fun observeMainViewModel() = with((activity as MainActivity).viewModel) {
        signOutCompleted.observe(viewLifecycleOwner, EventObserver {
//            findNavController().popBackStack(R.id.navigation_login, false);
            findNavController().navigate(R.id.action_profileFragment_toLoginFragment)
        })
    }
}