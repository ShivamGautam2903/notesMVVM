package com.example.notesmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()
        val repository = NoteRepository(dao)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)


        binding.btnAdd.setOnClickListener {
            var note = Note(0,binding.tvTitle.text.toString(),binding.tvBody.text.toString())
            mainViewModel.createNote(note)
            finish()
        }
    }
}