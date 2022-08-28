package com.example.notesmvvm.MainViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesmvvm.Database.Note
import com.example.notesmvvm.Repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    fun getNotes(): LiveData<List<Note>> {
        return noteRepository.getNotes()
    }

    fun createNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.createNote(note)
        }
    }
}