package com.gitsoft.notesapp.ui.collect

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.gitsoft.notesapp.R
import com.gitsoft.notesapp.database.NotesDatabase
import com.gitsoft.notesapp.databinding.NewNoteFragmentBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

class NewNoteFragment : Fragment() {

    private lateinit var viewModel: NewNoteViewModel
    private lateinit var binding: NewNoteFragmentBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewNoteFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val notesDatabaseDao = NotesDatabase.getDatabase(application).notesDatabaseDao
        val viewModelFactory = NewNoteViewModelFactory(notesDatabaseDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewNoteViewModel::class.java)
        binding.lifecycleOwner = this
        binding.newNoteViewModel = viewModel

        viewModel.showEmptySnackBarEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Empty note discarded",
                    LENGTH_SHORT
                ).show()

                viewModel.onEmptyNote()
            }
        })

        viewModel.navigateToDisplayFragment.observe(viewLifecycleOwner, { navigate ->
            if (navigate == true) {
                val navController = findNavController()
                navController.navigate(R.id.action_newNoteFragment_to_noteDisplay)
                viewModel.onNavigatedToDisplayFragment()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.save_action -> {
            viewModel.onSaveNote()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.edit_action)
        item.isVisible = false
    }

}