package com.thekingames.ui

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.widget.TextView


class ProgressBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    private var maxBarValue: Int = 100
    public var textVariant = 1
    // Установка максимального значения
    fun setMaxValue(maxValue: Int) {
        this.maxBarValue = maxValue
    }

    init {
        val damageTypeface = Typeface.createFromAsset(context.assets, "fonts/damage.ttf")
        typeface = damageTypeface
    }

    // Установка значения
    @Synchronized
    fun setValue(value: Double) {
        val newClipLevel = (value * 10000 / maxBarValue).toInt()
        when (textVariant) {
            1 -> text = getTextVariant1(value)
            2 -> text = getTextVariant2(value)
            3 -> text = getTextVariant3(value)
        }
        val background = this.background as LayerDrawable

        val barValue = background.getDrawable(1) as ClipDrawable

        if (barValue.level > newClipLevel) {
            barValue.alpha = 194
            postDelayed({ barValue.alpha = 255 }, 200)
        }
        barValue.level = newClipLevel
        // Уведомляем об изменении Drawable
        // Уведомляем об изменении Drawable
        drawableStateChanged()
    }



    private fun getTextVariant1(value: Double): String {
        return ((value * 100) / maxBarValue).toInt().toString() + "%"
    }

    private fun getTextVariant2(value: Double): String {
        return value.toInt().toString() + " / " + maxBarValue.toString() + "(${((100 * value) / maxBarValue).toInt()}%)"
    }

    private fun getTextVariant3(value: Double): String {
        return value.toInt().toString() + " / " + maxBarValue.toString()
    }
}