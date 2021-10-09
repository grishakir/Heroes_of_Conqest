package com.thekingames.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet


class FanTextView : android.support.v7.widget.AppCompatTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val fanTypeFace = Typeface.createFromAsset(context.assets, "fonts/fantasy.ttf")
        typeface = fanTypeFace
    }
}
