package com.xenderx.noteit.ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.xenderx.noteit.R
import com.xenderx.noteit.models.Note
import com.xenderx.noteit.repositories.NoteRepository
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity :
    AppCompatActivity(),
    View.OnTouchListener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener,
    View.OnClickListener
{
    companion object {
        private const val TAG = "NoteActivity"
    }

    // vars
    private var isNewNote: Boolean = true
    private var isEditMode: Boolean = false
    private var mNote: Note? = null
    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mNoteRepository: NoteRepository

    /**
     * Activity overrides
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        setSupportActionBar(toolbar)

        mGestureDetector = GestureDetector(this, this)
        mNoteRepository = NoteRepository(this)

        setUpListeners()

        mNote = intent.extras?.getParcelable("note")

        mNote?.let { note ->
            txt_title.text = note.title
            edit_content.setText(note.content)
            isNewNote = false
            isEditMode = false
            edit_content.keyListener = null
            edit_content.clearFocus()
        }

        if (isNewNote) {
            txt_title.text = "Title"
            isEditMode = true
            enterEditMode()
            edit_title.setSelection(edit_title.length())
            edit_title.requestFocus()
        }


    }

    override fun onBackPressed() {
        if (isEditMode) {
            btn_back_done.performClick()
        } else {
            super.onBackPressed()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isEditMode", isEditMode)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isEditMode = savedInstanceState.getBoolean("isEditMode")
        if (isEditMode) {
            enterEditMode()
        } else {
            exitEditMode()
        }
    }

    /**
     * Private functions
     */

    private fun setUpListeners() {
        btn_back_done.setOnClickListener(this)
        edit_content.setOnTouchListener(this)
        txt_title.setOnClickListener(this)
    }

    private fun enterEditMode(): Boolean {
        isEditMode = true
        edit_content.apply {
            keyListener = EditText(this@NoteActivity).keyListener
            isFocusable = true
            isFocusableInTouchMode = true
            isCursorVisible = true
            setTextIsSelectable(true)
            requestFocus()
        }

        btn_back_done.setImageResource(R.drawable.ic_check_24dp)
        txt_title.visibility = View.GONE
        edit_title.apply {
            visibility = View.VISIBLE
            setText(txt_title.text)
        }
        showSoftKeyboard()
        return true
    }

    private fun exitEditMode() {
        isEditMode = false
        edit_content.apply {
            keyListener = null
            isFocusable = false
            isFocusableInTouchMode = false
            isCursorVisible = false
            clearFocus()
        }

        btn_back_done.setImageResource(R.drawable.ic_arrow_back_24dp)

        edit_title.visibility = View.GONE
        txt_title.apply {
            visibility = View.VISIBLE
            text = edit_title.text.toString()
        }

        val newNote = Note().apply {
            title = edit_title.text.toString()
            content = edit_content.text.toString()
            timestamp = getCurrentDate()
        }

        hideSoftKeyboard()
        if (newNote != mNote) {
            mNote?.apply {
                title = newNote.title
                content = newNote.content
                timestamp = newNote.timestamp
            }
            saveNote()
        }
    }

    private fun saveNote() {
        if (isNewNote) {
            mNoteRepository.insertNote(Note(
                edit_title.text.toString(),
                edit_content.text.toString(),
                getCurrentDate()
            ))
            showToast("Note Created")
        } else {
            mNote?.let { mNoteRepository.updateNote(it) }
            showToast("Note Updated")
        }
    }

    private fun getCurrentDate(): String =
        SimpleDateFormat("MMM d, ''yy", Locale.getDefault())
            .format(Calendar.getInstance().time)

    private fun showSoftKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * OnTouchListener Overrides
     */

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return mGestureDetector.onTouchEvent(event)
    }

    /**
     * GestureDetector.OnGestureListener Overrides
     */

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
    }

    /**
     * GestureDetector.OnDoubleTapListener Overrides
     */

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return enterEditMode()
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }

    /**
     * View.OnClickListener Override
     */

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back_done -> {
                if (isEditMode) {
                    exitEditMode()
                } else {
                    finish()
                }
            }
            R.id.txt_title -> {
                enterEditMode()
                edit_title.requestFocus()
                edit_title.setSelection(edit_title.length())
            }
        }
    }
}
