package com.example.notesmvvm.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.notesmvvm.Database.Note
import com.example.notesmvvm.Database.NoteDatabase
import com.example.notesmvvm.MainViewModel.MainViewModel
import com.example.notesmvvm.MainViewModel.MainViewModelFactory
import com.example.notesmvvm.repository.NoteRepository
import com.example.notesmvvm.databinding.ActivityCreateNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val title = bundle?.getString("title", "")
        val body = bundle?.getString("body", "")
        val id = intent?.getIntExtra("id", 0) ?: 0
        val task = intent?.getStringExtra("task") ?: "Add Note"

        binding.apply {
            tvTitle.setText(title)
            tvBody.setText(body)
            btnAdd.text = task
        }

        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()
        val repository = NoteRepository(dao)

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]


        binding.btnAdd.setOnClickListener {
            val note = Note(id, binding.tvTitle.text.toString(), binding.tvBody.text.toString())
            mainViewModel.createNote(note)
            finish()
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }
}