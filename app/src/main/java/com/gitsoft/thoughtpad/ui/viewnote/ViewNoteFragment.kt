
/*
* Copyright 2024 Denis Githuku
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.gitsoft.thoughtpad.ui.viewnote

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gitsoft.thoughtpad.R
import com.gitsoft.thoughtpad.database.NotesDatabase
import com.gitsoft.thoughtpad.databinding.ViewNoteFragmentBinding
import com.gitsoft.thoughtpad.repository.NotesRepository
import com.google.android.material.snackbar.Snackbar

class ViewNoteFragment : Fragment() {

    private lateinit var viewModel: ViewNoteViewModel
    private lateinit var binding: ViewNoteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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

        viewModel.navigateToNoteDisplay.observe(
            viewLifecycleOwner,
            {
                if (it == true) {
                    val navController = findNavController()
                    navController.navigate(
                        ViewNoteFragmentDirections.actionViewNoteFragmentToNoteListFragment()
                    )
                    viewModel.onNavigatedToNoteDisplay()
                }
            }
        )

        viewModel.noteEmptyEvent.observe(
            viewLifecycleOwner,
            {
                if (true == it) {
                    Snackbar.make(binding.viewLayout, "Note cannot be empty", Snackbar.LENGTH_SHORT).show()
                }
            }
        )

        viewModel.deleteNoteEvent.observe(
            viewLifecycleOwner,
            {
                if (true == it) {
                    val snackbar =
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "Note deleted successfully",
                            Snackbar.LENGTH_LONG
                        )

                    snackbar.setAction("UNDO") { viewModel.undoDelete() }

                    snackbar.show()

                    //                snackBar.setAction(R.string.undo_text, MyUndoListener(application))
                    viewModel.onShowDeleteNoteEvent()
                }
            }
        )

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun shareDetails() {
        val sendIntent =
            Intent(Intent.ACTION_SEND).apply {
                //                val title = viewModel.noteTitle.value.toString()
                val text = viewModel.noteText.toString()
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
        try {
            startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            // Define what your app should do if no activity can handle the intent.
            Log.e("ViewNoteFragment", "shareDetails: " + e.message)
            Toast.makeText(requireContext(), "Format not supported", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.view_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.delete_item -> {
                viewModel.deleteNote()
                true
            }
            R.id.share_item -> {
                shareDetails()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
}
