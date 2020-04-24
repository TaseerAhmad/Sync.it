package com.vastalisy.syncit.adapter.note_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastalisy.syncit.adapter.RecyclerClickListener
import com.vastalisy.syncit.databinding.NoteListItemBinding
import com.vastalisy.syncit.model.Note

class NoteListAdapter(private val itemClickListener: RecyclerClickListener) :
    ListAdapter<Note, NoteListAdapter.NoteViewHolder>(NoteDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder = NoteViewHolder(getBinding(parent))
        viewHolder.addOnItemClickListener()
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bindItem(currentList[position])
    }

    private fun NoteViewHolder.addOnItemClickListener() {
        itemView.setOnClickListener {
            itemClickListener.onItemClicked(currentList[adapterPosition])
        }
    }

    class NoteViewHolder(private val binding: NoteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(note: Note) {
            binding.itemTitle.text = note.title
            binding.itemDate.text = note.date
        }
    }

    private fun getBinding(parent: ViewGroup): NoteListItemBinding {
        val inflater = LayoutInflater.from(parent.context)
        return NoteListItemBinding.inflate(inflater, parent, false)
    }

    class NoteDiffUtil : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

}