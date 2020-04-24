package com.vastalisy.syncit.dao

import com.google.android.gms.tasks.Task
import com.vastalisy.syncit.model.Note

interface CloudNoteDao {
    fun pushNote(uid: String, documentId: String, note: Note): Task<Void>
    fun deleteNote(uid: String, documentId: String): Task<Void>
}