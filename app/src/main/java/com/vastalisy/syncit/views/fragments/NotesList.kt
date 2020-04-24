package com.vastalisy.syncit.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vastalisy.syncit.NavigationListener
import com.vastalisy.syncit.adapter.RecyclerClickListener
import com.vastalisy.syncit.adapter.note_list.NoteListAdapter
import com.vastalisy.syncit.database.DatabaseOperation
import com.vastalisy.syncit.databinding.NotesListBinding
import com.vastalisy.syncit.model.Note
import com.vastalisy.syncit.viewmodel.NoteViewModel
import com.vastalisy.syncit.viewmodel.factory.NoteViewModelFactory

class NotesList : Fragment(), View.OnClickListener {
    private var _binding: NotesListBinding? = null
    private val notesListBinding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteListAdapter: NoteListAdapter
    private var layoutManager: RecyclerView.LayoutManager? = null
    lateinit var navigationActionListener: NavigationListener

    override fun onClick(v: View?) {
        notesListBinding.apply {
            when (v?.id) {
                emptyStateImg.id -> navigationActionListener.onEditorButtonClicked()
                openEditorButton.id -> navigationActionListener.onEditorButtonClicked()
                openSettingsButton.id -> navigationActionListener.onSettingButtonClicked()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        initRecyclerAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotesListBinding.inflate(inflater, container, false)
        return notesListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        noteViewModel.notesList().observe(viewLifecycleOwner, Observer {
            when (val operation = it) {
                is DatabaseOperation.Loading -> handleStateLoading()
                is DatabaseOperation.Loaded -> handleStateLoaded(operation.noteList)
            }
        })

        notesListBinding.also {
            it.emptyStateImg.setOnClickListener(this)
            it.openEditorButton.setOnClickListener(this)
            it.openSettingsButton.setOnClickListener(this)
        }

    }

    override fun onDestroyView() {
        layoutManager = null
        notesListBinding.noteListRecycler.adapter = null
        super.onDestroyView()

        _binding = null
    }

    private fun enableNoteStateEmpty() {
        val emptyStateLabel = notesListBinding.emptyStateLabel
        val emptyStateImg = notesListBinding.emptyStateImg
        val noteList = notesListBinding.noteListRecycler

        noteList.isVisible = false
        emptyStateImg.isVisible = true
        emptyStateLabel.isVisible = true
    }

    private fun disableNoteStateEmpty() {
        val noteList = notesListBinding.noteListRecycler
        val emptyStateImg = notesListBinding.emptyStateImg
        val emptyStateLabel = notesListBinding.emptyStateLabel

        emptyStateImg.isVisible = false
        emptyStateLabel.isVisible = false
        noteList.isVisible = true
    }

    private fun initViewModel() {
        val factory = NoteViewModelFactory(requireActivity().application)
        noteViewModel = ViewModelProvider(requireActivity(), factory)[NoteViewModel::class.java]
        requireActivity().lifecycle.addObserver(noteViewModel)
    }

    private fun initRecyclerAdapter() {
        noteListAdapter = NoteListAdapter(RecyclerClickListener {
            navigationActionListener.onEditorButtonClicked(it)
        })
    }

    private fun initRecyclerView() {
        with(notesListBinding) {
            layoutManager = LinearLayoutManager(requireContext())
            noteListRecycler.setHasFixedSize(true)
            noteListRecycler.layoutManager = layoutManager
            noteListRecycler.adapter = noteListAdapter
        }
    }

    private fun handleStateLoaded(it: List<Note>) {
        disableStateLoading()
        showNotes(it)
    }

    private fun handleStateLoading() {
        disableNoteStateEmpty()
        enableStateLoading()
    }

    private fun showNotes(it: List<Note>) {
        notesListBinding.noteCount.text = it.size.toString()
        if (it.isEmpty()) {
            enableNoteStateEmpty()
        } else {
            disableNoteStateEmpty()
        }

        noteListAdapter.submitList(it)
    }

    private fun enableStateLoading() {
        with(notesListBinding) {
            noteCount.isVisible = false
            noteCountLabel.isVisible = false
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun disableStateLoading() {
        with(notesListBinding) {
            noteCount.isVisible = true
            noteCountLabel.isVisible = true
            progressBar.visibility = View.GONE
        }
    }

}