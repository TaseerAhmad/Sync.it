package com.vastalisy.syncit.dao

import androidx.room.*
import com.vastalisy.syncit.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Dao
interface NoteDao {
    @Insert
    fun insertNote(note: Note): Long

    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Update
    fun updateNote(note: Note)

    @Query("SELECT id, title, text, date FROM note WHERE id = :id LIMIT 1")
    fun readNote(id: Int): Note

    @Delete
    fun deleteNote(note: Note)
}