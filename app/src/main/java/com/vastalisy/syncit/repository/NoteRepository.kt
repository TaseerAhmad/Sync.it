package com.vastalisy.syncit.repository

import android.app.Application
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.vastalisy.syncit.NOTE_SYNC_WORK
import com.vastalisy.syncit.SHOULD_DELETE
import com.vastalisy.syncit.SHOULD_NOT_DELETE
import com.vastalisy.syncit.database.DatabaseOperation
import com.vastalisy.syncit.database.NoteDatabase
import com.vastalisy.syncit.feature_sync.NoteSyncWorkRequest
import com.vastalisy.syncit.model.Note
import com.vastalisy.syncit.model.NoteOperation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class NoteRepository(application: Application) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val workManager = WorkManager.getInstance(application)
    private val noteDatabase = NoteDatabase.getInstance(application)

    suspend fun createNote(note: Note) = coroutineScope {
        withContext(Dispatchers.IO) {
            val id = noteDatabase.noteDao().insertNote(note)
            note.id = id.toInt()
            savePendingOperation(note)
            return@withContext id
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            val localUpdateJob = async { noteDatabase.noteDao().updateNote(note) }
            val cloudUpdateJob = async { savePendingOperation(note) }
            localUpdateJob.await()
            cloudUpdateJob.await()
        }
    }

    suspend fun readNote(id: Int) = coroutineScope {
        withContext(Dispatchers.IO) {
            return@withContext noteDatabase.noteDao().readNote(id)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            val localNoteDelete = async { noteDatabase.noteDao().deleteNote(note) }
            val cloudNoteDeleteJob = async { savePendingOperation(note, SHOULD_DELETE) }
            cloudNoteDeleteJob.await()
            localNoteDelete.await()
        }
    }

    fun allNotes() = flow {
        emit(DatabaseOperation.Loading)
        noteDatabase.noteDao().getAllNotes().collect { emit(DatabaseOperation.Loaded(it)) }
    }

    fun submitNoteSyncRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            if (noteDatabase.noteOpDao().hasPendingOperations()) {
                workManager.enqueueUniqueWork(
                    NOTE_SYNC_WORK,
                    ExistingWorkPolicy.KEEP,
                    NoteSyncWorkRequest.getWorkRequest()
                )
            }
        }
    }

    private suspend fun savePendingOperation(note: Note, delete: Int = SHOULD_NOT_DELETE) {
        coroutineScope {
            val noteOp = NoteOperation().mapTo(note, delete)
            noteDatabase.noteOpDao().insertPendingOperation(noteOp)
        }
    }

    private fun NoteOperation.mapTo(note: Note, shouldDel: Int = SHOULD_NOT_DELETE): NoteOperation {
        with(this) {
            isDeleted = shouldDel
            syncText = note.text
            syncDate = note.date
            syncTitle = note.title
            ownerUid = firebaseAuth.currentUser?.uid.toString()
            ownerMail = firebaseAuth.currentUser?.email.toString()
        }
        return this
    }

}