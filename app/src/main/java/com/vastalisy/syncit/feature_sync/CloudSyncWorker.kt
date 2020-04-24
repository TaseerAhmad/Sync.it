package com.vastalisy.syncit.feature_sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vastalisy.syncit.dao.NoteOperationDao
import com.vastalisy.syncit.database.CloudDatabase
import com.vastalisy.syncit.database.NoteDatabase
import com.vastalisy.syncit.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CloudSyncWorker(context: Context, param: WorkerParameters) : CoroutineWorker(context, param) {
    private val syncNotification = SyncNotification(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val cloudDatabase = CloudDatabase.getInstance()
        val localDatabase = NoteDatabase.getInstance(applicationContext)

        val pendingOperations = localDatabase.noteOpDao().getPendingOperations()


        run {
            val list =  pendingOperations.filter { it.isDeleted == 1 }
            if (list.isNotEmpty()) {
                list.forEach { pendingNote ->
                    val id = pendingNote.noteOpId.toString()

                    cloudDatabase.deleteNote(pendingNote.ownerUid, id).addOnSuccessListener {
                        clearPendingOperation(localDatabase.noteOpDao(), pendingNote.noteOpId!!)
                    }
                }
            }
        }

        run {
            val list = pendingOperations.filter { it.isDeleted == 0 }
            val note = Note()
            if (list.isNotEmpty()) {
                list.forEach { pendingNote ->
                    val id = pendingNote.noteOpId.toString()
//                    val localNote = localDatabase.noteDao().readNote(pendingNote.noteOpId!!)
                    note.date = pendingNote.syncDate
                    note.text = pendingNote.syncText
                    note.title = pendingNote.syncTitle


                    cloudDatabase.pushNote(pendingNote.ownerUid, id, note)
                        .addOnSuccessListener {
                            clearPendingOperation(localDatabase.noteOpDao(), pendingNote.noteOpId!!)
                        }
                }
            }
        }

        if (!localDatabase.noteOpDao().hasPendingOperations()) {
            syncNotification.makeSyncSuccessNotification()
            Result.success()
        } else {
            syncNotification.makeSyncErrorNotification()
            Result.failure()
        }

    }

    private fun clearPendingOperation(db: NoteOperationDao, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            db.clearCompletedOperation(id)
        }
    }

}