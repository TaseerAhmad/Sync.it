package com.vastalisy.syncit.feature_sync

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder

object NoteSyncWorkRequest {
    private val constraint = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val workRequest = OneTimeWorkRequestBuilder<CloudSyncWorker>()
        .setConstraints(constraint)
        .build()

    fun getWorkRequest(): OneTimeWorkRequest {
        return workRequest
    }
}