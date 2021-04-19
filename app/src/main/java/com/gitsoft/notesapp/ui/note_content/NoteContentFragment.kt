package com.gitsoft.notesapp.ui.note_content

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.gitsoft.notesapp.R
import com.gitsoft.notesapp.databinding.NoteContentFragmentBinding

class NoteContentFragment : Fragment() {
    private lateinit var viewModel: NoteContentViewModel
    private lateinit var binding: NoteContentFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NoteContentFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val note = NoteContentFragmentArgs.fromBundle(requireArguments()).selectedNote
        val viewModelFactory = NoteContentViewModelFactory(note, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NoteContentViewModel::class.java)
        binding.lifecycleOwner = this
        binding.noteContentViewModel = viewModel
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.save_action)
        item.isVisible = false
    }
}