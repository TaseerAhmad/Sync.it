package com.vastalisy.syncit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteOperation(
    @PrimaryKey(autoGenerate = false)
    var noteOpId: Int? = null,
    var isDeleted: Int = 0,
    var syncText: String = "",
    var syncTitle: String = "",
    var syncDate: String = "",
    var ownerUid: String = "",
    var ownerMail: String = ""
)