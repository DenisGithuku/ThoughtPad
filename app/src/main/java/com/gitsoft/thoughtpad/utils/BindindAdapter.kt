package com.gitsoft.thoughtpad.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gitsoft.thoughtpad.model.Note


@BindingAdapter("notes")
fun bindNotes(recyclerView: RecyclerView, data: List<Note>?) {
    val adapter = recyclerView.adapter as NotesAdapter
    adapter.submitList(data)
}
