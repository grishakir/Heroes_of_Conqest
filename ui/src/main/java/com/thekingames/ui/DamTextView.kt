package com.thekingames.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet


class DamTextView : android.support.v7.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {
        val fanTypeFace = Typeface.createFromAsset(context.assets, "fonts/damage.ttf")
        typeface = fanTypeFace
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val fanTypeFace = Typeface.createFromAsset(context.assets, "fonts/damage.ttf")
        typeface = fanTypeFace
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val fanTypeFace = Typeface.createFromAsset(context.assets, "fonts/damage.ttf")
        typeface = fanTypeFace
    }
}
