package com.example.notesmvvm.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun noteDao(): NoteDao

    companion object{
        private var INSTANCE : NoteDatabase? = null
        fun getDatabase(context: Context) : NoteDatabase{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,
                        NoteDatabase::class.java,
                    "note_database").build()
                }
            }
            return INSTANCE!!
        }
    }
}