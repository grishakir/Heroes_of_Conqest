package com.thekingames.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import android.widget.ScrollView

class XScroll : HorizontalScrollView {
    private var origX: Int = 0
    private var origY: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun canScrollHorizontally(direction: Int): Boolean {
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            origX = ev.x.toInt()
            origY = ev.y.toInt()
        } else if (ev.action == MotionEvent.ACTION_MOVE) {
            val deltaX = Math.abs(ev.x - origX)
            val deltaY = Math.abs(ev.y - origY)
            return deltaX >= THRESHOLD || deltaY >= THRESHOLD
        }
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return true
    }

    companion object {
        private const val THRESHOLD = 60f
    }
}
