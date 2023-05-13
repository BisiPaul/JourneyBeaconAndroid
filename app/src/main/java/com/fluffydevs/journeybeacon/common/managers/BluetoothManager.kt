package com.fluffydevs.journeybeacon.common.managers

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import com.fluffydevs.journeybeacon.R
import org.altbeacon.beacon.BeaconManager


class BluetoothManager(
    val activity: AppCompatActivity
) {
    fun verifyBluetooth() {
        try {
            if (!BeaconManager.getInstanceForApplication(activity).checkAvailability()) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
                builder.setTitle("Bluetooth not enabled")
                builder.setMessage("Please enable bluetooth in settings and restart this application.")
                builder.setPositiveButton(R.string.ok, null)
                builder.setOnDismissListener { finishAffinity(activity) }
                builder.show()
            }
        } catch (e: RuntimeException) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder.setTitle("Bluetooth LE not available")
            builder.setMessage("Sorry, this device does not support Bluetooth LE.")
            builder.setPositiveButton(R.string.ok, null)
            builder.setOnDismissListener { finishAffinity(activity) }
            builder.show()
        }
    }
}