package com.xenderx.noteit.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class NotesAdapterItemDecoration(private val bottomMargin: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = bottomMargin
    }
}