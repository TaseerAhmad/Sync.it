@file:Suppress("SpellCheckingInspection")

package com.vastalisy.syncit.dao

import androidx.room.*
import com.vastalisy.syncit.model.NoteOperation
import com.vastalisy.syncit.model.RemovableNote

@Dao
interface NoteOperationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPendingOperation(noteOperation: NoteOperation)

//    @Query("SELECT * FROM noteoperation WHERE isDeleted = 0")
//    fun getInsertPendingOperations(): List<NoteOperation>

//    @Query("SELECT * FROM noteoperation WHERE isDeleted = 1")
//    fun getDeletePendingOperations(): List<NoteOperation>

    @Query("SELECT * FROM noteoperation")
    fun getPendingOperations(): List<NoteOperation>

    @Query("SELECT EXISTS (SELECT * FROM noteoperation LIMIT 1)")
    fun hasPendingOperations(): Boolean

    @Query("DELETE FROM noteoperation WHERE noteOpId = :id")
    fun clearCompletedOperation(id: Int)

}