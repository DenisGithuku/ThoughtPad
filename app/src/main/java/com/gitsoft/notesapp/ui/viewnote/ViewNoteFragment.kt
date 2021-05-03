package com.gitsoft.notesapp.ui.viewnote

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gitsoft.notesapp.R
import com.gitsoft.notesapp.database.NotesDatabase
import com.gitsoft.notesapp.databinding.ViewNoteFragmentBinding
import com.gitsoft.notesapp.repository.NotesRepository
import com.google.android.material.snackbar.Snackbar

class ViewNoteFragment : Fragment() {

    private lateinit var viewModel: ViewNoteViewModel
    private lateinit var binding: ViewNoteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewNoteFragmentBinding.inflate(inflater)
        val arguments = ViewNoteFragmentArgs.fromBundle(requireArguments()).note
        val application = requireNotNull(this.activity).application
        val dao = NotesDatabase.getInstance(application).dao
        val repository = NotesRepository(dao)
        val viewModelFactory = ViewNoteViewModelFactory(repository, arguments, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewNoteViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToNoteDisplay.observe(viewLifecycleOwner, {
            if (it == true) {
                val navController = findNavController()
                navController.navigate(ViewNoteFragmentDirections.actionViewNoteFragmentToNoteListFragment())
                viewModel.onNavigatedToNoteDisplay()
            }
        })

        viewModel.noteEmptyEvent.observe(viewLifecycleOwner, {
            if (true == it) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Note cannot be empty",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.deleteNoteEvent.observe(viewLifecycleOwner, {
            if (true == it) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Note deleted successfully",
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.onShowDeleteNoteEvent()
            }
        })


        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.view_todo_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete_item -> {
            viewModel.deleteNote()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


}