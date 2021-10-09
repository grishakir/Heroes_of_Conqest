package com.thekingames.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.*


private const val PLAYER1 = 1
private const val PLAYER2 = 2

class HUD @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var random = Random(System.currentTimeMillis())
    private var thresholdGreatDamage = 150
    private val inflater = LayoutInflater.from(context)




    fun drawPlayer1Damage(value: Double) {
        drawAction(value, PLAYER1, null)
    }

    fun drawPlayer2Damage(value: Double) {
        drawAction(value, PLAYER2, null)
    }

    fun drawPlayer1Damage(value: Double, spell: SpellView) {
        drawAction(value, PLAYER1, ContextCompat.getDrawable(context, spell.getSkillIcon().iconId))
    }

    fun drawPlayer2Damage(value: Double, spell: SpellView) {
        drawAction(value, PLAYER2, ContextCompat.getDrawable(context, spell.getSkillIcon().iconId))
    }

    fun drawPlayer1Damage(value: Double, effectView: EffectView) {
        drawAction(value, PLAYER1, ContextCompat.getDrawable(context,effectView.idSrc))
    }

    fun drawPlayer2Damage(value: Double, effectView: EffectView) {
        drawAction(value, PLAYER2, ContextCompat.getDrawable(context,effectView.idSrc))
    }

    private fun drawAction(value: Double, playerId: Int, icon: Drawable?) {
        when {
            value > 0 && value < thresholdGreatDamage -> inflater.inflate(R.layout.text_damage, this, true)
            value < 0 -> {
                inflater.inflate(R.layout.text_heal, this, true)
            }
            else -> inflater.inflate(R.layout.text_great_damage, this, true)
        }
        val damageView = getChildAt(this.childCount - 1) as LinearLayout
        val textView = damageView.findViewById<TextView>(R.id.text)
        val iconLeft = damageView.findViewById<ImageView>(R.id.icon_left)
        iconLeft.background = null

        val iconRight = damageView.findViewById<ImageView>(R.id.icon_right)
        iconRight.background = null
        if (icon != null) {
            if (playerId == 1) {
                iconLeft.background = icon

            } else if (playerId == 2) {
                iconRight.background = icon
            }
        }
        if(height>=10) {
            if (playerId == 1) {
                damageView.x = (width.toFloat() / 2) - 1.5f * (Utils.toPixels(120f, context))
                damageView.y = ((height / 3) + random.nextInt(height / 10)).toFloat()  //from 40% to 60%*/
            } else if (playerId == 2) {
                damageView.x = (width.toFloat() / 2) + 0.5f * (Utils.toPixels(120f, context))
                damageView.y = ((height / 3) + random.nextInt(height / 10)).toFloat()  //from 40% to 60%*/
            }
            textView.text = Math.abs(value.toInt()).toString()
        }
    }

    fun tick() {
        (0 until childCount)
                .map { getChildAt(it) }
                .forEach {
                    if (it.alpha >= 0.2f) {
                        it.alpha -= 0.1f
                        it.y -= Utils.toPixels(9f, context)
                        if (it.x < (width.toFloat() / 2)) {
                            it.x -= Utils.toPixels(4f, context)
                        } else {
                            it.x += Utils.toPixels(4f, context)
                        }
                    } else {
                        removeView(it)
                    }
                }
    }
}