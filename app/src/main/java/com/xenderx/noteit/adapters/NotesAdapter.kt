package com.xenderx.noteit.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xenderx.noteit.R
import com.xenderx.noteit.models.Note
import kotlinx.android.synthetic.main.cardview_note.view.*

class NotesAdapter(
    private val context: Context,
    private var listenerOn: OnNoteClickListener?
): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var notes = ArrayList<Note>()

    constructor(
        context: Context
    ) : this(context, null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(context).inflate(R.layout.cardview_note, parent, false),
            listenerOn
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.titleTextView.text = notes[position].title
        holder.timestampTextView.text = notes[position].timestamp
    }

    override fun getItemCount(): Int = notes.size

    fun setNotesList(notes: List<Note>) {
        this.notes = notes as ArrayList<Note>
        notifyDataSetChanged()
    }

    fun addNote(note: Note) {
        notes.add(0, note)
        notifyItemInserted(0)
        notifyItemChanged(0)
    }

    fun addAllNotes(notes: List<Note>) {
        notes.forEach {
            this.notes.add(0, it)
            notifyItemInserted(0)
            notifyItemChanged(0)
        }
    }

    fun deleteNote(index: Int) {
        notes.removeAt(index)
        notifyItemRemoved(index)
        notifyItemChanged(index)
    }

    fun deleteNote(note: Note) {
        val index = notes.indexOf(note)
        if (index < 0) {
            throw NoSuchElementException("this note does not exist")
        }
        notes.remove(note)
        notifyItemRemoved(index)
        notifyItemChanged(index)
    }

    fun getNote(index: Int): Note {
        return notes[index]
    }

    fun setOnNoteClickListener(listenerOn: OnNoteClickListener) {
        this.listenerOn = listenerOn
    }

    fun clear() {
        notes.clear()
    }

    class NotesViewHolder(itemView: View, listenerOn: OnNoteClickListener?): RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.txt_title
        val timestampTextView: TextView = itemView.txt_timestamp

        init {
            itemView.setOnClickListener { view ->
                listenerOn?.let {
                    if (adapterPosition != RecyclerView.NO_POSITION) listenerOn.onNoteClick(
                        view,
                        adapterPosition
                    )
                }
            }
        }

    }

    interface OnNoteClickListener {
        fun onNoteClick(view: View, position: Int)
    }
}