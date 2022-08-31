package com.example.notesmvvm.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesmvvm.Database.Note
import com.example.notesmvvm.R

class NoteRVAdapter(
    private val noteClickInterface: NoteClickInterface) :
    RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.tvRvDisplay)
        val iconDelete: ImageView = itemView.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTitle.text = allNotes[position].title
        holder.noteTitle.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes[position])
        }
        holder.iconDelete.setOnClickListener {
            noteClickInterface.onNoteDelete(allNotes[position])
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }
}

interface NoteClickInterface {
    fun onNoteDelete(note: Note)
    fun onNoteClick(note: Note)
}