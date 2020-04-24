package com.vastalisy.syncit.feature_sync

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.vastalisy.syncit.R
import com.vastalisy.syncit.feature_notification.Base
import com.vastalisy.syncit.feature_notification.Base.Companion.channelId
import com.vastalisy.syncit.feature_notification.Base.Companion.syncNotifyChannelId

class SyncNotification(private val context: Context) {
    private val notificationManager = initNotificationManager()

    init {
        createNotificationChannel()
    }

    fun makeSyncSuccessNotification() {
        val notify = NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getText(R.string.syncSuccessNotificationTitle))
            .setContentText(context.getText(R.string.syncSuccessNotificationDesc))
            .setSmallIcon(R.drawable.ic_sync)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(syncNotifyChannelId, notify)
    }

    fun makeSyncErrorNotification() {
        val notify = NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getText(R.string.syncErrorNotificationTitle))
            .setContentText(context.getText(R.string.syncErrorNotificationDesc))
            .setStyle(NotificationCompat.BigTextStyle())
            .setStyle(NotificationCompat.BigTextStyle())
            .setSmallIcon(R.drawable.ic_sync_problem)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(syncNotifyChannelId, notify)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelImportance = NotificationManager.IMPORTANCE_LOW
            val channelDescription = "This channel notifies if the notes were synced or failed."
            NotificationChannel(channelId, Base.channelName, channelImportance).apply {
                description = channelDescription
                notificationManager.createNotificationChannel(this)
            }
        }
    }

    private fun initNotificationManager(): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}