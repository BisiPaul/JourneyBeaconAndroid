package com.fluffydevs.journeybeacon.common.managers

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.RequestCodes
import com.fluffydevs.journeybeacon.common.utils.isAccessBackgroundLocationGranted
import com.fluffydevs.journeybeacon.common.utils.isAccessFineLocationGranted
import com.google.android.material.snackbar.Snackbar

class PermissionsManager(
    val activity: AppCompatActivity
) {
    private val requestAccessFineLocationPermission =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // ALL GOOD
                if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) == false) {
                    // False Positive
                    showAccessFineLocationDialog()
                }
            } else {
                showAccessFineLocationDialog()
            }
        }

    private fun showAccessFineLocationDialog() {
        showPermissionRationaleDialog(
            rationaleResId = R.string.permission_access_fine_location_rationale,
            intentAction = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            permissionRequestCode = RequestCodes.ACCESS_FINE_LOCATION,
            snackbarTextResId = R.string.permission_access_fine_location_denied
        )
    }

    private val requestAccessBackgroundLocationPermission =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // ALL GOOD
                if (isPermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == false) {
                    // False Positive
                    showAccessBackgroundLocationDialog()
                }
            } else {
                showAccessBackgroundLocationDialog()
            }
        }

    private fun showAccessBackgroundLocationDialog() {
        showPermissionRationaleDialog(
            rationaleResId = R.string.permission_access_background_location_rationale,
            intentAction = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            permissionRequestCode = RequestCodes.ACCESS_BACKGROUND_LOCATION,
            snackbarTextResId = R.string.permission_access_background_location_denied
        )
    }

    private fun isPermissionGranted(permission: String): Boolean? {
        return when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                activity.isAccessFineLocationGranted()
            }
            Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                activity.isAccessBackgroundLocationGranted()
            }
            else -> null
        }
    }

    fun requestPermission(permission: String) {
        when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) == false)
                    requestAccessFineLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                if (isPermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == false)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        requestAccessBackgroundLocationPermission.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    }
            }
        }
    }

    private fun showPermissionRationaleDialog(
        rationaleResId: Int,
        positiveButtonTextResId: Int = R.string.permission_ok,
        intentAction: String,
        permissionRequestCode: Int,
        negativeButtonTextResId: Int = R.string.permission_cancel,
        snackbarTextResId: Int
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(activity.getString(rationaleResId))
            .setPositiveButton(
                activity.getString(positiveButtonTextResId)
            ) { _, _ ->
                try {
                    val intent = Intent(intentAction)
                    intent.data = Uri.parse("package:" + activity.packageName)
                    startActivityForResult(
                        activity,
                        intent,
                        permissionRequestCode,
                        null
                    )
                } catch(exception: Exception) {
                    exception.printStackTrace()
                } finally {
                    val intent = Intent(intentAction)
                    startActivityForResult(
                        activity,
                        intent,
                        permissionRequestCode,
                        null
                    )
                }
            }
            .setNegativeButton(activity.getString(negativeButtonTextResId)) { dialog, _ ->
                // Handle cancel button
                dialog.dismiss()

                Snackbar.make(
                    activity.findViewById(R.id.containerMain),
                    activity.getString(snackbarTextResId),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}