package com.vastalisy.syncit.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vastalisy.syncit.*
import com.vastalisy.syncit.databinding.NoteEditorBinding
import com.vastalisy.syncit.model.Note
import com.vastalisy.syncit.viewmodel.NoteViewModel
import com.vastalisy.syncit.viewmodel.factory.NoteViewModelFactory

class NoteEditor : Fragment() {
    private var note = Note()
    private var deleteButtonClicked = false
    private var _binding: NoteEditorBinding? = null
    private val noteEditorBinding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoteEditorBinding.inflate(inflater, container, false)
        return noteEditorBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run {
            extractArgument(this) {
                note = it
                setNote(it)
            }
        }

        noteEditorBinding.deleteNoteButton.setOnClickListener {
            deleteButtonClicked = true
            requireActivity().supportFragmentManager.popBackStack()
        }

        noteEditorBinding.closeEditorButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        setToolTips()
    }

    override fun onPause() {
        super.onPause()

        if (!deleteButtonClicked) {
            extractNoteText { title, text ->
                note.title = title
                note.text = text
            }
            noteViewModel.storeNote(note) { note = it }
        } else {
            noteViewModel.removeNote(note)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private inline fun extractArgument(arg: Bundle, block: (Note) -> Unit) {
        with(Note()) {
            id = arg.getInt(NOTE_ID)
            title = arg.getString(NOTE_TITLE, "Error")
            text = arg.getString(NOTE_TEXT, "Error")
            date = arg.getString(NOTE_DATE, "Error`")
            block(this)
        }
    }

    private fun initViewModel() {
        val factory = NoteViewModelFactory(requireActivity().application)
        noteViewModel = ViewModelProvider(requireActivity(), factory)[NoteViewModel::class.java]
    }

    private fun setToolTips() {
        TooltipCompat.setTooltipText(
            noteEditorBinding.deleteNoteButton,
            getString(R.string.delete_button_tooltip)
        )
        TooltipCompat.setTooltipText(
            noteEditorBinding.closeEditorButton,
            getString(R.string.close_button_tooltip)
        )
    }

    private fun setNote(note: Note) {
        with(noteEditorBinding) {
            noteDate.text = note.date
            noteText.setText(note.text)
            noteTitle.setText(note.title)
        }
    }

    private inline fun extractNoteText(block: (String, String) -> Unit) {
        val text = noteEditorBinding.noteText.text.toString()
        val title = noteEditorBinding.noteTitle.text.toString()
        block(title, text)
    }

}