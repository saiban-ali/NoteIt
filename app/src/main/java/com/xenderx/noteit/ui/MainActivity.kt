package com.xenderx.noteit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xenderx.noteit.R
import com.xenderx.noteit.adapters.NotesAdapter
import com.xenderx.noteit.repositories.NoteRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    // vars
    private lateinit var mRecyclerAdapter: NotesAdapter
    private lateinit var mNoteRepository: NoteRepository
    private val mItemTouchHelper =
        object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mNoteRepository.deleteNote(mRecyclerAdapter.getNote(viewHolder.adapterPosition))
                mRecyclerAdapter.deleteNote(viewHolder.adapterPosition)
            }

        }

    /**
     * Activity overrides
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        mNoteRepository = NoteRepository(this)

        /*val notes = ArrayList<Note>().apply {
            add(Note("title", "content", "Dec 20"))
            add(Note("title", "content", "Dec 20"))
            add(Note("title", "content", "Dec 20"))
            add(Note("title", "content", "Dec 20"))
            add(Note("title", "content", "Dec 20"))
            add(Note("title", "content", "Dec 20"))
            add(Note("title", "content", "Dec 20"))
            add(Note("title", "content", "Dec 20"))
            add(Note("title", "content", "Dec 20"))
        }*/

        setupRecyclerView()
        setupListeners()

    }

    /**
     * Private functions
     */

    private fun setupRecyclerView() {
        mRecyclerAdapter = NotesAdapter(this)
        recyclerview_notes.adapter = mRecyclerAdapter

        mRecyclerAdapter.setOnNoteClickListener(object : NotesAdapter.OnNoteClickListener {
            override fun onNoteClick(view: View, position: Int) {
                showToast("NoteClicked")
                startActivity(
                    Intent(this@MainActivity, NoteActivity::class.java).apply {
                        putExtras(Bundle().apply { putParcelable("note", mRecyclerAdapter.getNote(position)) })
                    }
                )
            }
        })

        ItemTouchHelper(mItemTouchHelper).attachToRecyclerView(recyclerview_notes)
        populateRecyclerView()
    }

    private fun populateRecyclerView() {
        mNoteRepository.getNotes().observe(this, Observer {
            mRecyclerAdapter.setNotesList(it)
        })
    }

    private fun setupListeners() {
        fab_new_note.setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
