
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
package com.gitsoft.thoughtpad.ui.addnote

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gitsoft.thoughtpad.R
import com.gitsoft.thoughtpad.database.NotesDatabase
import com.gitsoft.thoughtpad.databinding.AddEditNoteFragmentBinding
import com.gitsoft.thoughtpad.repository.NotesRepository
import com.google.android.material.snackbar.Snackbar

class AddEditNoteFragment : Fragment() {

    private lateinit var viewModel: AddEditNoteViewModel
    private lateinit var binding: AddEditNoteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddEditNoteFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dao = NotesDatabase.getInstance(application).dao
        val repository = NotesRepository(dao)
        val viewModelFactory = AddEditNoteViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddEditNoteViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToNoteDisplay.observe(
            viewLifecycleOwner,
            { navigate ->
                if (true == navigate) {
                    val navController = findNavController()
                    navController.navigate(
                        AddEditNoteFragmentDirections.actionAddEditNoteFragmentToNoteListFragment()
                    )
                    viewModel.onNavigatedToNoteDisplay()
                }
            }
        )

        viewModel.noteAddedEvent.observe(
            viewLifecycleOwner,
            {
                if (it == true) {
                    Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.note_added_messge),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()

                    viewModel.finishedShowingSnackBar()
                }
            }
        )

        viewModel.noteEmptyEvent.observe(
            viewLifecycleOwner,
            {
                if (it == true) {
                    Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.empty_note_message),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()

                    viewModel.finishedShowingSnackBar()
                }
            }
        )

        viewModel.backgroundChanged.observe(
            viewLifecycleOwner,
            { it.let { binding.editLayout.setBackgroundColor(Color.argb(100, 10, 221, 224)) } }
        )

        return binding.root
    }
}
