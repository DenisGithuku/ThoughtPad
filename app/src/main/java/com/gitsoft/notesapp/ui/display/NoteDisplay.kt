package com.gitsoft.notesapp.ui.display

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gitsoft.notesapp.R
import com.gitsoft.notesapp.adapters.NotesAdapter
import com.gitsoft.notesapp.database.NotesDatabase
import com.gitsoft.notesapp.databinding.NoteDisplayFragmentBinding

class NoteDisplay : Fragment() {

    private lateinit var viewModel: NoteDisplayViewModel
    private lateinit var binding: NoteDisplayFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NoteDisplayFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val notesDatabaseDao = NotesDatabase.getDatabase(application).notesDatabaseDao
        val viewModelFactory = NoteDisplayViewModelFactory(notesDatabaseDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NoteDisplayViewModel::class.java)
        binding.lifecycleOwner = this
        binding.notesDisplayViewModel = viewModel

        viewModel.navigateToNewNote.observe(viewLifecycleOwner, { navigate ->
            if (navigate == true) {
                val navController = findNavController()
                navController.navigate(R.id.action_noteDisplay_to_newNoteFragment)
                viewModel.onDoneNavigatingToNewNote()
            }
        })

        val adapter = NotesAdapter(NotesAdapter.NoteClickListener {
            viewModel.onNavigateToNoteContent(it)
        })
        binding.listItem.adapter = adapter

        viewModel.navigateToNoteContent.observe(viewLifecycleOwner, {
            if (null != it) {
                val navController = findNavController()
                navController.navigate(NoteDisplayDirections.actionShowNoteData(it))
                viewModel.onDoneNavigatingToNoteContent()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val checkBtn: MenuItem = menu.findItem(R.id.save_action)
        val editBtn: MenuItem = menu.findItem(R.id.edit_action)
        checkBtn.isVisible = false
        editBtn.isVisible = false
    }

}