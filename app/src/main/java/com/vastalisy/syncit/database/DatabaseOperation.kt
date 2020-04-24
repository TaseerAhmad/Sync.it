package com.vastalisy.syncit.database

import com.vastalisy.syncit.model.Note

sealed class DatabaseOperation {
    object Loading : DatabaseOperation()
    class Loaded(var noteList: List<Note>) : DatabaseOperation()
}