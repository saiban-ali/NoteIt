package com.xenderx.noteit.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.xenderx.noteit.models.Note

@Dao
interface NoteDao {

    @Insert
    fun insert(vararg notes: Note): LongArray

    @Query("SELECT * FROM NOTES ORDER BY id DESC")
    fun getNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM NOTES WHERE title LIKE :title")
    fun getNotes(title: String): LiveData<List<Note>>

    @Delete
    fun delete(vararg notes: Note): Int

    @Update
    fun update(vararg notes: Note): Int
}