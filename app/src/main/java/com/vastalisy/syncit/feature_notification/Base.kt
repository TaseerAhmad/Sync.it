package com.vastalisy.syncit.feature_notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class Base : Application() {

    companion object {
        const val syncNotifyChannelId = 1
        const val channelName = "Note Sync"
        const val channelId = "NS-1"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelImportance = NotificationManager.IMPORTANCE_DEFAULT
            val channelDescription = "This channel notifies if the notes were synced or failed."


            NotificationChannel(channelId, channelName, channelImportance).apply {
                description = channelDescription
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(this)
            }
        }
    }

}