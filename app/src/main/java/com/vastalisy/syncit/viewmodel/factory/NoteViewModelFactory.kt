package com.vastalisy.syncit.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vastalisy.syncit.viewmodel.NoteViewModel

@Suppress("UNCHECKED_CAST")
class NoteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(NoteViewModel(application)) as T
    }
}