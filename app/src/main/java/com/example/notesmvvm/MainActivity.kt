package com.example.notesmvvm

import android.app.Activity
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
import com.example.notesmvvm.RecyclerView.RecyclerViewAdapter
import com.example.notesmvvm.Repository.NoteRepository
import com.example.notesmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.rvDisplay
        val adapter = RecyclerViewAdapter()

        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()
        val repository = NoteRepository(dao)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.getNotes().observe(this) {
            adapter.submitList(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter


        binding.addNote.setOnClickListener{
            val intent = Intent(this, CreateNoteActivity::class.java).also{
                startActivity(it)
            }
        }
    }

}