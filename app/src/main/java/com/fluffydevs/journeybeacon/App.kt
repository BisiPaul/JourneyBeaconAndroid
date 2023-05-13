package com.fluffydevs.journeybeacon

import android.app.Application
import com.fluffydevs.journeybeacon.domain.service.NotificationCreator
import dagger.hilt.android.HiltAndroidApp
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region
import timber.log.Timber


@HiltAndroidApp
class App : Application(), MonitorNotifier {
    val wildcardRegion = Region("wildcardRegion", null, null, null)
    var insideRegion = false

    lateinit var beaconManager: BeaconManager

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        setupBeaconManager()
    }

    private fun setupBeaconManager() {
        beaconManager = BeaconManager.getInstanceForApplication(this)

        // This unlocks the ability to continually scan for long periods of time in the background on Andorid 8+
        // in exchange for showing an icon at the top of the screen and a always-on notification to
        // communicate to users that your app is using resources in the background.

        val notificationCreator = NotificationCreator(applicationContext)
        beaconManager.enableForegroundServiceScanning(notificationCreator.getNotification(), 456)

        // For the above foreground scanning service to be useful, you need to disable
        // JobScheduler-based scans (used on Android 8+) and set a fast background scan
        // cycle that would otherwise be disallowed by the operating system.
        //
        beaconManager.setEnableScheduledScanJobs(false)
        beaconManager.backgroundBetweenScanPeriod = 0
        beaconManager.backgroundScanPeriod = 1100

        Timber.d(TAG, "setting up background monitoring in app onCreate")
        beaconManager.addMonitorNotifier(this)

        // If we were monitoring *different* regions on the last run of this app, they will be
        // remembered.  In this case we need to disable them here
        // If we were monitoring *different* regions on the last run of this app, they will be
        // remembered.  In this case we need to disable them here
        for (region in beaconManager.monitoredRegions) {
            beaconManager.stopMonitoring(region)
        }

        beaconManager.startMonitoring(wildcardRegion)
    }

    override fun didEnterRegion(region: Region?) {
        Timber.d("did enter region.")
        insideRegion = true
        // Send a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Timber.d("Sending notification.")
   }

    override fun didExitRegion(region: Region?) {
        insideRegion = false
        // do nothing here. logging happens in MonitoringActivity
    }

    override fun didDetermineStateForRegion(state: Int, region: Region?) {
        // do nothing here. logging happens in MonitoringActivity
    }

    companion object {
        private const val TAG = "JourneyBeaconApp"
    }
}