package com.thekingames.ui

import android.content.Context
import android.view.View
import android.view.View.inflate
import android.widget.ImageView
import android.widget.TextView

open class EffectView(var context: Context, var idSrc: Int, private val idFon: Int, private val idDispelFon: Int) {
    val view: View = inflate(context, R.layout.effect, null)
    val image: ImageView = view.findViewById(R.id.icon)
    private val timer: TextView = view.findViewById(R.id.text)
    private val counter: TextView = view.findViewById(R.id.counter)

    init {
        image.setImageResource(idSrc)
    }

    fun setText(value: String) {
        timer.text = value
    }

    fun setDispelEnable(isDispel: Boolean) {
        if (isDispel) {
            image.setBackgroundResource(idDispelFon)
        } else {
            image.setBackgroundResource(idFon)
        }
    }

    fun setCount(value: Int) {
        if (value > 1) {
            counter.text = value.toString()
        } else {
            counter.text = ""
        }
    }

    fun copy(): EffectView {
        return EffectView(context, idSrc, idFon, idDispelFon)
    }
}

open class PositiveEffectView(context: Context, idSrc: Int) : EffectView(context, idSrc, R.drawable.fon_positive_effect, R.drawable.fon_positive_dispel_effect)

open class NegativeEffectView(context: Context, idSrc: Int) : EffectView(context, idSrc, R.drawable.fon_negative_effect, R.drawable.fon_negative_dispel_effect)

class EffectDefDown(context: Context) : NegativeEffectView(context, R.drawable.effect_def_down)
class EffectDefUp(context: Context) : PositiveEffectView(context, R.drawable.effect_def_up)
class EffectFreezing(context: Context) : NegativeEffectView(context, R.drawable.effect_freezing)
class EffectMadness(context: Context) : PositiveEffectView(context, R.drawable.effect_madness)
class EffectPoison(context: Context) : NegativeEffectView(context, R.drawable.effect_poison)
class EffectReflection(context: Context) : PositiveEffectView(context, R.drawable.effect_reflection)
class EffectSilence(context: Context) : NegativeEffectView(context, R.drawable.effect_silence)
class EffectStun(context: Context) : NegativeEffectView(context, R.drawable.effect_stunned)
class EffectTrap(context: Context) : NegativeEffectView(context, R.drawable.effect_trap)
class EffectVengeful(context: Context) : PositiveEffectView(context, R.drawable.effect_vengeful)
class EffectWound(context: Context) : NegativeEffectView(context, R.drawable.effect_wound)
class EffectSmoke(context: Context) : PositiveEffectView(context, R.drawable.effect_smoke)
class EffectMastery(context: Context) : PositiveEffectView(context, R.drawable.effect_mastery)
class EffectHolyAura(context: Context) : PositiveEffectView(context, R.drawable.effect_holy_aura)
class EffectThunderstorm(context: Context) : NegativeEffectView(context, R.drawable.effect_thunderstorm)
class EffectWitchcraft(context: Context) : PositiveEffectView(context, R.drawable.effect_witchcraft)
class EffectHealing(context: Context) : PositiveEffectView(context, R.drawable.effect_healing)
class EffectBerserk(context: Context) : PositiveEffectView(context, R.drawable.effect_berserk)
class EffectManaBurn(context: Context) : NegativeEffectView(context, R.drawable.effect_mana_burn)
class EffectDestroyWeapon(context: Context) : NegativeEffectView(context, R.drawable.effect_disarming)
class EffectMagicShield(context: Context) : PositiveEffectView(context, R.drawable.effect_magic_shield)