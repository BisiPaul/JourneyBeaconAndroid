package com.fluffydevs.journeybeacon.presentation.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.structure.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainViewModel>()

    lateinit var googleSignInClient: GoogleSignInClient

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.primaryNavigationFragment?.let { fragment ->
                // Intercept fragments that should not be allowed to back navigate
                if (fragment is BaseFragment<*, *> && !fragment.allowBackButton()) {
                    return
                }

                // Disable Acky callback
                isEnabled = false
                // Do the default onBackPressed
                this@MainActivity.onBackPressedDispatcher.onBackPressed()
                // Enable Acky callback
                isEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Handle BottomNavigationView visibility based on destinations
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_splash, R.id.navigation_login -> {
                    navView.visibility = View.GONE
                }

                else -> {
                    navView.visibility = View.VISIBLE
                }
            }
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        setupGoogleSignIn()
    }

    override fun onStart() {
        super.onStart()

        checkIfUserIsSignedIn()
    }

    private fun setupGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun checkIfUserIsSignedIn() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            viewModel.onNewUser()
        } else {
            viewModel.onUserAlreadySignedIn(account)

        }
    }

    fun signOut() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this) {
                viewModel.onSignOut()
            }
    }
}