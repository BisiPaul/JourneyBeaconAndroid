package com.fluffydevs.journeybeacon.domain.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.Constants
import com.fluffydevs.journeybeacon.presentation.main.MainActivity

class NotificationCreator(private val context: Context) {
    private var scanningForDevicesNotification: Notification? = null
    private val notificationChannelId = Constants.NOTIFICATION_CHANNEL_ID

    val builder: Notification.Builder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(
                context,
                notificationChannelId
            )
        } else {
            Notification.Builder(context)
        }

    fun getNotification() : Notification {
        // Return notification if already created
        scanningForDevicesNotification?.let { notification ->
            return notification
        }

        // Create notification and store it
        createNotification().let {
            scanningForDevicesNotification = it
            return it
        }
    }

    private fun createNotification(): Notification {
        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION
                it.enableLights(true)
                it.lightColor = Color.BLUE
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent =
            Intent(context, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        // This ensures that the 10s delay before service is started is not applied for SDK 31 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O)
            builder.setPriority(Notification.PRIORITY_HIGH) // for under SDK 26 compatibility

        return builder
            .setContentTitle("Scanning for Beacons")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }
}