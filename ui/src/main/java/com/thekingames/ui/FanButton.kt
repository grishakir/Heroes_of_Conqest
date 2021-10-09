package com.thekingames.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button


class FanButton : Button {
    constructor(context: Context) : super(context) {
        val fanTypeFace = Typeface.createFromAsset(context.assets, "fonts/fantasy.ttf")
        typeface = fanTypeFace
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val fanTypeFace = Typeface.createFromAsset(context.assets, "fonts/fantasy.ttf")
        typeface = fanTypeFace
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val fanTypeFace = Typeface.createFromAsset(context.assets, "fonts/fantasy.ttf")
        typeface = fanTypeFace
    }
}
