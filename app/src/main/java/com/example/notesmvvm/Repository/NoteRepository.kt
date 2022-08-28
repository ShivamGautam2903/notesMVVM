package com.example.notesmvvm.Repository

import androidx.lifecycle.LiveData
import com.example.notesmvvm.Database.Note
import com.example.notesmvvm.Database.NoteDao

class NoteRepository(private val noteDao: NoteDao) {

    fun getNotes(): LiveData<List<Note>>{
        return noteDao.getNotes()
    }

    suspend fun createNote(note: Note){
        noteDao.createNote(note)
    }

    suspend fun deleteNote(note: Note){
        noteDao.deleteNote(note)
    }
}