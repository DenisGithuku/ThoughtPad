package com.gitsoft.thoughtpad.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gitsoft.thoughtpad.databinding.NoteItemBinding
import com.gitsoft.thoughtpad.model.Note
import com.gitsoft.thoughtpad.ui.noteslist.NoteListViewModel

class NotesAdapter(private val viewModel: NoteListViewModel): ListAdapter<Note, NotesAdapter.ViewHolder>(DiffCallBack) {
    class ViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note, viewModel: NoteListViewModel) {
            binding.note = note
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NoteItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, viewModel)
    }
}