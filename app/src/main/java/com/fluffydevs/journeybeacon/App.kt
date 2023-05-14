package com.fluffydevs.journeybeacon

import android.app.Application
import android.os.Parcel
import com.fluffydevs.journeybeacon.domain.service.NotificationCreator
import dagger.hilt.android.HiltAndroidApp
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region
import timber.log.Timber


@HiltAndroidApp
class App : Application(), MonitorNotifier {
    // Region to detect all the AltBeacons
    val wildcardRegion = Region("wildcardRegion", null, null, null)
    var insideRegionBack = false
    var insideRegionFront = false

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

        val backRegion = Region("0x01020304050607080910111213141516", BLUETOOTH_ADDRESS_BACK)
        val frontRegion = Region("0x01020304050607080910111213141517", BLUETOOTH_ADDRESS_FRONT)
        beaconManager.startMonitoring(backRegion)
        beaconManager.startMonitoring(frontRegion)
    }

    override fun didEnterRegion(region: Region?) {
        Timber.d("did enter region.")
        when (region?.bluetoothAddress) {
            BLUETOOTH_ADDRESS_BACK -> { insideRegionBack = true }
            BLUETOOTH_ADDRESS_FRONT -> { insideRegionFront = true }
        }
        // Send a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Timber.d("Sending notification.")
   }

    override fun didExitRegion(region: Region?) {
        when (region?.bluetoothAddress) {
            BLUETOOTH_ADDRESS_BACK -> { insideRegionBack = false }
            BLUETOOTH_ADDRESS_FRONT -> { insideRegionFront = false }
        }
        // do nothing here. logging happens in MonitoringActivity
    }

    fun handleInsideRegion() : Boolean {
        return insideRegionBack || insideRegionFront
    }

    override fun didDetermineStateForRegion(state: Int, region: Region?) {
        // do nothing here. logging happens in MonitoringActivity
    }

    companion object {
        private const val TAG = "JourneyBeaconApp"

        private const val BLUETOOTH_ADDRESS_BACK = "B8:27:EB:E4:A5:E4"
        private const val BLUETOOTH_ADDRESS_FRONT = "DC:A6:32:F0:5E:54"
    }
}