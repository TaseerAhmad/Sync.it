package com.vastalisy.syncit

import com.vastalisy.syncit.model.Note

interface NavigationListener {
    fun onSettingButtonClicked()
    fun onEditorButtonClicked(note: Note? = null)
}