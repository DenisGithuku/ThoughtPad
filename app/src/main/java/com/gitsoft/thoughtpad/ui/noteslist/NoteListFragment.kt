
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
package com.gitsoft.thoughtpad.ui.noteslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gitsoft.thoughtpad.R
import com.gitsoft.thoughtpad.database.NotesDatabase
import com.gitsoft.thoughtpad.databinding.NoteListFragmentBinding
import com.gitsoft.thoughtpad.repository.NotesRepository
import com.gitsoft.thoughtpad.utils.NotesAdapter

class NoteListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: NoteListViewModel
    private lateinit var binding: NoteListFragmentBinding
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NoteListFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dao = NotesDatabase.getInstance(application).dao
        val repository = NotesRepository(dao)
        val viewModelFactory = NoteListViewModelFactory(application, repository)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, viewModelFactory).get(NoteListViewModel::class.java)
        binding.viewModel = viewModel

        adapter = NotesAdapter(viewModel)
        binding.listItem.adapter = adapter

        viewModel.navigateToAddNote.observe(
            viewLifecycleOwner,
            {
                if (true == it) {
                    val navController = findNavController()
                    navController.navigate(
                        NoteListFragmentDirections.actionNoteListFragmentToAddEditNoteFragment()
                    )
                    viewModel.onNavigatedToAddNote()
                }
            }
        )

        viewModel.navigateToViewNote.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    val navController = findNavController()
                    navController.navigate(
                        NoteListFragmentDirections.actionNoteListFragmentToViewNoteFragment(it)
                    )

                    viewModel.onNavigateToViewNote()
                }
            }
        )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_list_menu, menu)

        val search = menu.findItem(R.id.app_bar_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = false
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.settings -> {
                val navController = findNavController()
                navController.navigate(
                    NoteListFragmentDirections.actionNoteListFragmentToSettingsFragment()
                )
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        viewModel
            .searchDatabase(searchQuery)
            .observe(viewLifecycleOwner, { it.let { adapter.submitList(it) } })
    }
}
