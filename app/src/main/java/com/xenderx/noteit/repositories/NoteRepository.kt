package com.xenderx.noteit.repositories

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.xenderx.noteit.models.Note
import com.xenderx.noteit.room.NoteDatabase

class NoteRepository(private val context: Context) {

    private val mNoteDatabase by lazy { NoteDatabase.getInstance(context) }

    fun insertNote(note: Note) {
        AsyncTask.execute {
            mNoteDatabase!!.getNoteDao().insert(note)
        }
    }

    fun updateNote(note: Note) {
        AsyncTask.execute {
            mNoteDatabase!!.getNoteDao().update(note)
        }
    }

    fun deleteNote(note: Note) {
        AsyncTask.execute {
            mNoteDatabase!!.getNoteDao().delete(note)
        }
    }

    fun getNotes(): LiveData<List<Note>> {
        return mNoteDatabase!!.getNoteDao().getNotes()
    }

}