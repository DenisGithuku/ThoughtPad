package com.gitsoft.notesapp.utils

import android.view.View
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gitsoft.notesapp.model.Note
import com.google.android.material.textview.MaterialTextView


@BindingAdapter("notes")
fun bindNotes(recyclerView: RecyclerView, data: List<Note>?) {
    val adapter = recyclerView.adapter as NotesAdapter
    adapter.submitList(data)
}

@BindingAdapter("visibilityStatus")
fun bindVisibility(layout: RelativeLayout, status: NoteStatus?) {
    when (status) {
        NoteStatus.EMPTY -> {
            layout.visibility = View.VISIBLE
        }
        NoteStatus.OCCUPIED -> {
            layout.visibility = View.GONE
        }
    }
}