package com.example.notesmvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesmvvm.Database.Note
import com.example.notesmvvm.Database.NoteDatabase
import com.example.notesmvvm.MainViewModel.MainViewModel
import com.example.notesmvvm.MainViewModel.MainViewModelFactory
import com.example.notesmvvm.RecyclerView.NoteClickInterface
import com.example.notesmvvm.RecyclerView.NoteRVAdapter
import com.example.notesmvvm.repository.NoteRepository
import com.example.notesmvvm.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), NoteClickInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var noteList: List<Note>
    private lateinit var tempNotes: MutableList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.rvDisplay
        val adapter = NoteRVAdapter(this)

        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()
        val repository = NoteRepository(dao)

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        //observing the data and saving the value in a List
        mainViewModel.getNotes().observe(this) {
            adapter.updateList(it)
            noteList = it
            tempNotes = noteList.toMutableList()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        binding.addNote.setOnClickListener {
            Intent(this, CreateNoteActivity::class.java).also {
                startActivity(it)
            }
        }

        /* The next block implements SearchView functioning. We create a tempNote mutable List and whenever we make a query
        the answer is saved in it. At each step we update the adapter list with the new temp list.
        When we're done*/
        binding.apply {
            searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchview.clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    tempNotes.clear()
                    val searchText = newText!!.lowercase(Locale.getDefault())
                    if (searchText.isNotEmpty()) {
                        noteList.forEach {
                            if (it.title.lowercase(Locale.getDefault()).contains(searchText)) {
                                tempNotes.add(it)
                            }
                        }
                        adapter.updateList(tempNotes)
                        adapter.notifyDataSetChanged()
                    } else {
                        tempNotes.clear()
                        adapter.updateList(noteList)
                        adapter.notifyDataSetChanged()
                    }
                    return false
                }
            })
        }
    }

    /*
        This function handles the onClickEvent for each item in recyclerview.
        We use the position we obtain from the adapter, and obtain it's ID using a previously created noteList variable.
        */

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, CreateNoteActivity::class.java)
        intent.putExtra("id", note.id)
        intent.putExtra("title", note.title)
        intent.putExtra("body", note.body)
        intent.putExtra("task", "Edit Note")
        startActivity(intent)
    }

    override fun onNoteDelete(note: Note) {
        mainViewModel.deleteNote(note)
        Toast.makeText(this, "The note has been deleted", Toast.LENGTH_SHORT).show()
    }

}