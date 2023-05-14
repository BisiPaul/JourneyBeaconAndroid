package com.fluffydevs.journeybeacon.presentation.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fluffydevs.journeybeacon.App
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.managers.BluetoothManager
import com.fluffydevs.journeybeacon.common.managers.PermissionsManager
import com.fluffydevs.journeybeacon.common.structure.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MonitorNotifier {

    val viewModel by viewModels<MainViewModel>()

    lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var permissionsManager: PermissionsManager

    private lateinit var bluetoothManager: BluetoothManager

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

        permissionsManager = PermissionsManager(this)
        bluetoothManager = BluetoothManager(this)

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

    fun requestStartingPermissions() {
        permissionsManager.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionsManager.requestPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsManager.requestPermission(Manifest.permission.BLUETOOTH_SCAN)
        }
    }

    fun verifyBluetooth() {
        bluetoothManager.verifyBluetooth()
    }

    fun startMonitoringBeacons() {
        BeaconManager.getInstanceForApplication(this).addMonitorNotifier(this);
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun didEnterRegion(region: Region?) {
        runOnUiThread {
            if ((application as App).handleInsideRegion() == true) {
                viewModel.onRegionEnter()
            }
            Timber.d("HAS ENTERED REGIONNNNNNNNNN ${region?.bluetoothAddress}")
            Timber.d("region $region")
        }
    }

    override fun didExitRegion(region: Region?) {
        runOnUiThread {
            if ((application as App).handleInsideRegion() == false) {
                viewModel.onRegionExit()
            }
            Timber.d("HAS EXITED REGIONNNNNNNNNN")
            Timber.d("region $region")
        }
    }

    override fun didDetermineStateForRegion(state: Int, region: Region?) {
        // TODO @Paul
        Timber.d("HAS DETERMINED STATE FOR REGIONNNNNNNNNN, STATE: $state")
        Timber.d("region $region")
    }
}