package com.example.notesmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.notesmvvm.Database.Note
import com.example.notesmvvm.Database.NoteDatabase
import com.example.notesmvvm.MainViewModel.MainViewModel
import com.example.notesmvvm.MainViewModel.MainViewModelFactory
import com.example.notesmvvm.Repository.NoteRepository
import com.example.notesmvvm.databinding.ActivityCreateNoteBinding

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val title = bundle?.getString("title", "")
        val body = bundle?.getString("body", "")
        val vis = bundle?.getBoolean("vis") ?: false
//        val id = bundle?.getInt("id", 0)
//        val action = bundle?.getString("action", "Add Note")

        var id = intent?.getIntExtra("id",0) ?: 0
        var action = intent?.getStringExtra("action")  ?: "Add Note"

        binding.apply {
            tvTitle.setText(title)
            tvBody.setText(body)
            btnAdd.setText(action)

            if(vis){
                btnDelete.isVisible = true
            }
        }

        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()
        val repository = NoteRepository(dao)

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        binding.btnDelete.setOnClickListener {
            var note = Note(id!!,binding.tvTitle.text.toString(),binding.tvBody.text.toString())
            mainViewModel.deleteNote(note)
            finish()
        }

        binding.btnAdd.setOnClickListener {
            var note = Note(id!!, binding.tvTitle.text.toString(), binding.tvBody.text.toString())
            mainViewModel.createNote(note)
            finish()
        }

    }
}