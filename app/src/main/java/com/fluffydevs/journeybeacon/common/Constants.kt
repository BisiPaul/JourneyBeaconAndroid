package com.fluffydevs.journeybeacon.common

object Constants {

    const val BASE_URL = "https://journeybeacon-journeybeacon.azuremicroservices.io"

    const val NOTIFICATION_CHANNEL_ID = "ENDLESS_ALARM_SERVICE_CHANNEL_ID"
    const val NOTIFICATION_CHANNEL_NAME = "ENDLESS_ALARM_SERVICE_CHANNEL_NAME"
    const val NOTIFICATION_CHANNEL_DESCRIPTION = "ENDLESS_ALARM_SERVICE_CHANNEL"
}

object RequestCodes {
    const val RC_SIGN_IN = 20000
    const val ACCESS_FINE_LOCATION = 20001
    const val ACCESS_BACKGROUND_LOCATION = 20002
    const val BLUETOOTH_SCAN = 20003
}