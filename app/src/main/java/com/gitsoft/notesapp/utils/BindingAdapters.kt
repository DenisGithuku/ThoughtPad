package com.gitsoft.notesapp.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gitsoft.notesapp.adapters.NotesAdapter
import com.gitsoft.notesapp.database.Note


@BindingAdapter("notesData")
fun bindNoteList(recyclerView: RecyclerView, data: List<Note>?) {
    val adapter = recyclerView.adapter as NotesAdapter
    adapter.submitList(data)
}
