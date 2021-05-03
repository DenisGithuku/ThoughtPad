package com.gitsoft.notesapp.ui.noteslist

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gitsoft.notesapp.R
import com.gitsoft.notesapp.database.NotesDatabase
import com.gitsoft.notesapp.databinding.NoteListFragmentBinding
import com.gitsoft.notesapp.model.Note
import com.gitsoft.notesapp.repository.NotesRepository
import com.gitsoft.notesapp.utils.NotesAdapter

class NoteListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: NoteListViewModel
    private lateinit var binding: NoteListFragmentBinding
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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

        viewModel.navigateToAddNote.observe(viewLifecycleOwner, {
            if (true == it) {
                val navController = findNavController()
                navController.navigate(NoteListFragmentDirections.actionNoteListFragmentToAddEditNoteFragment())
                viewModel.onNavigatedToAddNote()
            }
        })

        viewModel.navigateToViewNote.observe(viewLifecycleOwner, {
            if (it != null) {
                openDetails(it)
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun openDetails(note: Note) {
        val navController = findNavController()
        navController.navigate(NoteListFragmentDirections.actionNoteListFragmentToViewNoteFragment(
            note
        ))

        viewModel.onNavigateToViewNote()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_list_menu, menu)

        val search = menu.findItem(R.id.app_bar_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.delete_all -> {
            viewModel.onClearAll()
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
        val search = "%$query%"

        viewModel.searchDatabase(search).observe(viewLifecycleOwner, {
            it.let {
                adapter.submitList(it)
            }
        })
    }

}