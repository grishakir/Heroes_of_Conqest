package com.thekingames.ui

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.LevelListDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.TextView
import java.text.DecimalFormat

abstract class SkillIcon @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    private val scaleAnimation = ScaleAnimation(1f, 1.35f, 1f, 1.35f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
    private val alphaAnimation = AlphaAnimation(1f, 0.3f)
    private val animationSet = AnimationSet(true)
    private val df = DecimalFormat("#.#")
    var rest: Double = 0.0
        set(value) {
            val percent: Double
            if (value > 0) {
                field = value
                if (value < 5)
                    this.text = df.format(value) else {
                    this.text = value.toInt().toString()
                }
                percent = (value / callDown)
            } else {
                field = 0.0
                this.text = ""
                percent = 0.0
                blinkAnimationStart()
            }
            val layer = this.background as LayerDrawable
            val callDownDrawable = layer.getDrawable(2) as TimerDrawable
            callDownDrawable.setPercent(percent)
            drawableStateChanged()
        }

    var callDown: Double = 1.0

    private var gCallDown: Double = 1.0

    var gRest: Double = gCallDown
        set(value) {
            val gPercent: Double
            if (value > 0) {
                field = value
                gPercent = (value / gCallDown)
            } else {
                field = 0.0
                gPercent = 0.0
            }

            val layer = this.background as LayerDrawable
            val callDownDrawable = layer.getDrawable(2) as TimerDrawable
            callDownDrawable.setGPercent(gPercent)
            callDownDrawable.invalidateSelf()
            drawableStateChanged()
        }

    private fun blinkAnimationStart() {
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)
        startAnimation(animationSet)
    }

    private fun alphaAnimationStart() {
        animationSet.addAnimation(alphaAnimation)
        startAnimation(animationSet)
    }

    fun touch(callDown: Double): Boolean {
        if (rest == 0.0 && gRest == 0.0) {
            this.callDown = callDown
            rest = callDown
            alphaAnimationStart()
            return true
        }
        return false
    }

    fun globalCallDown(gCallDown: Double) {
        if (gRest == 0.0) {
            this.gCallDown = gCallDown
            gRest = gCallDown
        }
    }

    private lateinit var layer: LayerDrawable

    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0,
            iconId: Int, fonId: Int) : this(context, attrs, defStyleAttr) {
        createBackGroundDrawable(fonId, iconId)
        val damageTypeface = Typeface.createFromAsset(context.assets, "fonts/damage.ttf")
        typeface = damageTypeface
        scaleAnimation.repeatCount = 1
        scaleAnimation.repeatMode = Animation.REVERSE
        scaleAnimation.duration = 110

        alphaAnimation.repeatCount = 1
        alphaAnimation.repeatMode = Animation.REVERSE
        alphaAnimation.duration = 110
    }

    var iconId: Int = R.drawable.skill_crushing_blow

    private fun createBackGroundDrawable(fonId: Int, iconId: Int) {
        this.iconId = iconId
        val fon: Drawable = ContextCompat.getDrawable(context, fonId)
        val icon = ContextCompat.getDrawable(context, iconId)
        val border: Drawable = ContextCompat.getDrawable(context, R.drawable.spell_border)
        val callDownDrawable = TimerDrawable()
        layer = LayerDrawable(arrayOf(fon, icon, callDownDrawable, border))
        val inset = Utils.toDp(5F, context).toInt()
        layer.setLayerInset(1, inset, inset, inset, inset)
        this.background = layer
    }

    fun activate() {
        (layer.getDrawable(0) as LevelListDrawable).level = 1
    }

    fun unActivate() {
        (layer.getDrawable(0) as LevelListDrawable).level = 0
    }
}