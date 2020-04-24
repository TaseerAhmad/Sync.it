package com.vastalisy.syncit.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.vastalisy.syncit.database.DatabaseOperation
import com.vastalisy.syncit.model.Note
import com.vastalisy.syncit.repository.NoteRepository
import com.vastalisy.syncit.util.CalendarUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    private val noteRepository = NoteRepository(application)
    private val notesList = MutableLiveData<DatabaseOperation>()

    fun storeNote(note: Note, onNoteStored: (Note) -> Unit) { //TODO CHANGE SCOPE TO APP
        viewModelScope.launch {
            insertNote(note, onNoteStored)
        }
    }

    fun removeNote(note: Note) {
        if (note.id != null) {
            viewModelScope.launch {
                deleteNote(note)
            }
        }
    }

    fun notesList(): LiveData<DatabaseOperation> {
        viewModelScope.launch {
            collectNoteList()
        }
        return notesList
    }

    private suspend fun collectNoteList() {
        withContext(Dispatchers.IO) {
//            noteRepository.allNotes()
            //TODO Move flow operator to the DAO
            noteRepository.allNotes().distinctUntilChanged().collect {
                when (it) {
                    is DatabaseOperation.Loaded -> notesList.postValue(it)
                    is DatabaseOperation.Loading -> notesList.postValue(it)
                }
            }
        }
    }

    private suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteRepository.deleteNote(note)
        }
    }

    private suspend inline fun insertNote(note: Note, crossinline onNoteStored: (Note) -> Unit) {
        if (note.id != null) {
            val savedNote = noteRepository.readNote(note.id!!)
            if (!areNotesSame(note, savedNote)) {
                noteRepository.updateNote(note)
                onNoteStored(note)
            }
        } else {
            if (!isNoteEmpty(note)) {
                if (note.title.isEmpty())
                    note.title = "Untitled note"

                note.date = CalendarUtil.getCurrentDate()
                note.id = noteRepository.createNote(note).toInt()
                onNoteStored(note)
            }
        }
    }

    private fun areNotesSame(p0: Note, p1: Note): Boolean {
        return p0.title == p1.title && p0.text == p1.text
    }

    private fun isNoteEmpty(note: Note): Boolean {
        return note.title.trim().isEmpty() && note.text.trim().isEmpty()
    }

    @Suppress("ProtectedInFinal")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun onAppStopped() {
        noteRepository.submitNoteSyncRequest()
    }

}