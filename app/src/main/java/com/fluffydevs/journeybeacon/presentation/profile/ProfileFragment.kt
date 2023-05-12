package com.fluffydevs.journeybeacon.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.structure.BaseFragment
import com.fluffydevs.journeybeacon.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val layoutId: Int = R.layout.fragment_profile

    override val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setControls()
        observe()
    }

    private fun setControls() {

    }

    private fun observe() = with(viewModel) {

    }
}