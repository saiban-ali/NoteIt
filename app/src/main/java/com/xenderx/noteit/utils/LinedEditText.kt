package com.xenderx.noteit.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.xenderx.noteit.R

class LinedEditText(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatEditText(context, attributeSet) {

    private val mRect = Rect()
    private val mPaint = Paint()

    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 2f
        mPaint.color = ContextCompat.getColor(context, R.color.colorLine)
    }

    override fun onDraw(canvas: Canvas?) {
        val height = (parent as View).height
        val numberOfLines = height / lineHeight
        var baseLine = getLineBounds(0, mRect)

        val rect = mRect
        val paint = mPaint

        for (i in 0..numberOfLines) {
            canvas?.drawLine(
                rect.left.toFloat(),
                (baseLine + 5).toFloat(),
                rect.right.toFloat(),
                (baseLine + 5).toFloat(),
                paint
            )

            baseLine += lineHeight
        }

        super.onDraw(canvas)
    }
}