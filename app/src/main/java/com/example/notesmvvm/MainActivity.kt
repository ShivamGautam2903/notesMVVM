package com.example.notesmvvm
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesmvvm.Database.Note
import com.example.notesmvvm.Database.NoteDatabase
import com.example.notesmvvm.MainViewModel.MainViewModel
import com.example.notesmvvm.MainViewModel.MainViewModelFactory
import com.example.notesmvvm.RecyclerView.NoteClickInterface
import com.example.notesmvvm.RecyclerView.NoteDeleteInterface
import com.example.notesmvvm.RecyclerView.NoteRVAdapter
import com.example.notesmvvm.repository.NoteRepository
import com.example.notesmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NoteClickInterface, NoteDeleteInterface{

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var noteList: List<Note>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.rvDisplay
        val adapter = NoteRVAdapter(this, this)

        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()
        val repository = NoteRepository(dao)


        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        //observing the data and saving the value in a List
        mainViewModel.getNotes().observe(this) {
            adapter.updateList(it)
            noteList = it
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        binding.addNote.setOnClickListener {
            Intent(this, CreateNoteActivity::class.java).also {
                startActivity(it)
            }
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