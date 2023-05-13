package com.fluffydevs.journeybeacon.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.Constants.RequestCodes.RC_SIGN_IN
import com.fluffydevs.journeybeacon.common.structure.BaseFragment
import com.fluffydevs.journeybeacon.common.structure.EventObserver
import com.fluffydevs.journeybeacon.databinding.FragmentLoginBinding
import com.fluffydevs.journeybeacon.presentation.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val layoutId: Int = R.layout.fragment_login

    override val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setControls()
        observe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            (activity as MainActivity).viewModel.account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            viewModel.onLoginSuccess()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            viewModel.onLoginFailure()
        }
    }

    private fun setControls() {
        binding.loginButton.setOnClickListener {
            val signInIntent: Intent = (activity as MainActivity).googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun observe() = with(viewModel) {
        loginSuccess.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        })
    }
}