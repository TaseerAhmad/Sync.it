package com.vastalisy.syncit.database

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.vastalisy.syncit.NOTE_DATE
import com.vastalisy.syncit.NOTE_TEXT
import com.vastalisy.syncit.NOTE_TITLE
import com.vastalisy.syncit.dao.CloudNoteDao
import com.vastalisy.syncit.model.Note

abstract class CloudDatabase : CloudNoteDao {
    private val database = getFireStore()
    private val noteMap = HashMap<String, String>()

    companion object {
        private lateinit var cloudDb: CloudDatabase

        fun getInstance(): CloudNoteDao {
            if (!::cloudDb.isInitialized)
                cloudDb = object : CloudDatabase() {}

            return cloudDb
        }
    }

    override fun pushNote(uid: String, documentId: String, note: Note): Task<Void> {
        noteMap.mapTo(note)
        val task = database.collection(uid).document(documentId).set(noteMap)
        noteMap.clear()
        return task
    }

    override fun deleteNote(uid: String, documentId: String): Task<Void> {
        return database.collection(uid).document(documentId).delete()
    }

    private fun getFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings
                .Builder()
                .setPersistenceEnabled(false)
                .build()
        }
    }

    private fun HashMap<String, String>.mapTo(note: Note) {
        put(NOTE_TEXT, note.text)
        put(NOTE_DATE, note.date)
        put(NOTE_TITLE, note.title)
    }
}