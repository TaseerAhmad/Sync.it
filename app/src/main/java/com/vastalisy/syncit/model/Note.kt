package com.vastalisy.syncit.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String = "",
    var text: String = "",
    var date: String = ""
)