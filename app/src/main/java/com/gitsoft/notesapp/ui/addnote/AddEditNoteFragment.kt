package com.gitsoft.notesapp.ui.addnote

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gitsoft.notesapp.R
import com.gitsoft.notesapp.database.NotesDatabase
import com.gitsoft.notesapp.databinding.AddEditNoteFragmentBinding
import com.gitsoft.notesapp.repository.NotesRepository
import com.google.android.material.snackbar.Snackbar

class AddEditNoteFragment : Fragment() {

    private lateinit var viewModel: AddEditNoteViewModel
    private lateinit var binding: AddEditNoteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditNoteFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dao = NotesDatabase.getInstance(application).dao
        val repository = NotesRepository(dao)
        val viewModelFactory = AddEditNoteViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddEditNoteViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToNoteDisplay.observe(viewLifecycleOwner, { navigate ->
            if (true == navigate) {
                val navController = findNavController()
                navController.navigate(AddEditNoteFragmentDirections.actionAddEditNoteFragmentToNoteListFragment())
                viewModel.onNavigatedToNoteDisplay()
            }
        })


        viewModel.noteAddedEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                "New note added",
                Snackbar.LENGTH_SHORT
                ).show()

                viewModel.finishedShowingSnackBar()
            }
        })

        viewModel.noteEmptyEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Note cannot be empty",
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.finishedShowingSnackBar()
            }
        })

        viewModel.backgroundChanged.observe(viewLifecycleOwner, {
            it.let {
                binding.viewLayout.setBackgroundColor(Color.argb(100, 10, 221, 224))
            }
        })

        return binding.root
    }

}