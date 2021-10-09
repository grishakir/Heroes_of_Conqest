package com.thekingames.medivalwarriors2.core.model

import android.content.Context
import com.thekingames.medivalwarriors2.core.model.interfaces.ActionEffect
import com.thekingames.medivalwarriors2.core.model.interfaces.InstantActionEffect
import com.thekingames.medivalwarriors2.core.model.interfaces.OnTickListener
import com.thekingames.medivalwarriors2.core.model.interfaces.PeriodicActionEffect
import com.thekingames.ui.*
import java.text.DecimalFormat
import java.util.concurrent.CopyOnWriteArrayList

const val SECOND = 1

open class Effect(var effectView: EffectView, val duration: Double, val isDispel: Boolean) : OnTickListener {
    init {
        effectView.setDispelEnable(isDispel)
    }

    var rest: Double = duration
        set(value) {
            field = if (value > 0) {
                value
            } else {
                0.0
            }
        }
    private lateinit var holder: Unit
    private lateinit var initiator: Unit
    protected open var timeBetweenTicks = SECOND
    private var sequencer: Int = 0

    private val df = DecimalFormat("#.#")

    var currentCount: Int = 1
        set(value) {
            rest = duration
            sequencer = 0
            field = if (value < maxCount) {
                value
            } else {
                maxCount
            }
            this.effectView.setCount(field)
        }
    open val maxCount = 1

    val actionEffects: CopyOnWriteArrayList<ActionEffect> = CopyOnWriteArrayList()

    private fun tick() {
        this.rest -= 0.1
        if (rest > 5) {
            this.effectView.setText(rest.toInt().toString())
        } else {
            this.effectView.setText(df.format(rest))
        }

        if (this.rest == 0.0) {
            dispersion()
        }
    }

    fun dispersion() {
        currentCount = 1
        actionEffects.forEach { it.remove(holder, initiator) }
        holder.onMoveEffectsListeners.forEach { it.onRemove(this) }
        this.holder.effects.remove(this)
    }


    open fun start(holder: Unit, initiator: Unit): Effect {
        this.holder = holder
        this.initiator = initiator
        actionEffects.forEach { it.put(holder, initiator) }
        this.effectView.setCount(currentCount)
        holder.onMoveEffectsListeners.forEach { it.onPut(this) }
        return this
    }

    fun start(holder: Unit): Effect {
        return start(holder, holder)
    }


    override fun onTick() {
        tick()
        sequencer++
        if (sequencer % (timeBetweenTicks * 10) == 0) {
            actionEffects.forEach { it.tick(currentCount, holder, initiator) }
        }
    }

    open fun copy(): Effect {
        val newEffect = Effect(effectView.copy(), duration, isDispel)
        newEffect.actionEffects.addAll(actionEffects)
        return newEffect
    }
}


open class NegativeEffect(view: EffectView, duration: Double, isDispel: Boolean) : Effect(view, duration, isDispel)

open class PositiveEffect(view: EffectView, duration: Double, isDispel: Boolean) : Effect(view, duration, isDispel)

open class BurstEffect(view: EffectView, duration: Double, isDispel: Boolean) : PositiveEffect(view, duration, isDispel)

open class HOT(view: EffectView, duration: Double, isDispel: Boolean) : PositiveEffect(view, duration, isDispel)

open class DOT(view: EffectView, duration: Double, isDispel: Boolean) : NegativeEffect(view, duration, isDispel)

class StunEffect(effectView: EffectView, duration: Double, isDispel: Boolean) :
        NegativeEffect(effectView, duration, isDispel) {
    private val actionEffect = object : InstantActionEffect() {
        override fun put(holder: Unit, initiator: Unit) {
            holder.isControl = false
            holder.stunDiminishing.start(0.5)
        }

        override fun remove(holder: Unit, initiator: Unit) {
            holder.isControl = true
        }
    }

    init {
        actionEffects.add(actionEffect)
    }

    override fun start(holder: Unit, initiator: Unit): Effect {
        rest *= holder.stunDiminishing.value
        return super.start(holder, initiator)
    }
}

class SilenceEffect(effectView: EffectView, duration: Double, isDispel: Boolean) :
        NegativeEffect(effectView, duration, isDispel) {
    private val actionEffect = object : InstantActionEffect() {
        override fun put(holder: Unit, initiator: Unit) {
            holder.isSpeaking = false
            holder.silenceDiminishing.start(0.5)
        }

        override fun remove(holder: Unit, initiator: Unit) {
            holder.isSpeaking = true

        }
    }

    init {
        actionEffects.add(actionEffect)
    }

    override fun start(holder: Unit, initiator: Unit): Effect {
        rest *= holder.silenceDiminishing.value
        return super.start(holder, initiator)
    }
}

class DisarmingEffect(effectView: EffectView, duration: Double, isDispel: Boolean) :
        NegativeEffect(effectView, duration, isDispel) {
    private val actionEffect = object : InstantActionEffect() {
        override fun put(holder: Unit, initiator: Unit) {
            holder.isArming = false
            holder.disarmingDiminishing.start(0.5)
        }

        override fun remove(holder: Unit, initiator: Unit) {
            holder.isArming = true

        }
    }

    init {
        actionEffects.add(actionEffect)
    }

    override fun start(holder: Unit, initiator: Unit): Effect {
        rest *= holder.disarmingDiminishing.value
        return super.start(holder, initiator)
    }
}

class ReflectionEffect(context: Context, duration: Double, isDispel: Boolean) :
        PositiveEffect(EffectReflection(context), duration, isDispel) {
    private val actionEffect = object : InstantActionEffect() {
        override fun put(holder: Unit, initiator: Unit) {
            initiator.focus.target = initiator.focus
        }

        override fun remove(holder: Unit, initiator: Unit) {
            initiator.focus.target = initiator
        }
    }

    init {
        actionEffects.add(actionEffect)
    }
}

class SmokeEffect(context: Context, duration: Double, isDispel: Boolean) :
        PositiveEffect(EffectSmoke(context), duration, isDispel) {
    private val actionEffect = object : InstantActionEffect() {
        override fun put(holder: Unit, initiator: Unit) {
            initiator.focus.target = null
        }

        override fun remove(holder: Unit, initiator: Unit) {
            initiator.focus.target = initiator
        }
    }

    init {
        actionEffects.add(actionEffect)
    }
}

class WitchcraftEffect(context: Context, duration: Double, override var maxCount: Int) :
        PositiveEffect(EffectWitchcraft(context), duration, false) {
    private val actionEffect = object : PeriodicActionEffect() {

        override fun tick(count: Int, holder: Unit, initiator: Unit) {
            holder.mana += 0.8 * count
        }
    }

    init {
        actionEffects.add(actionEffect)
    }
}


class DefenseUpEffect(context: Context, duration: Double, isDispel: Boolean, value: Double) :
        PositiveEffect(EffectDefUp(context), duration, isDispel) {
    private val actionEffect = object : InstantActionEffect() {
        override fun put(holder: Unit, initiator: Unit) {
            holder.def += value
        }

        override fun remove(holder: Unit, initiator: Unit) {
            holder.def -= value
        }
    }

    init {
        actionEffects.add(actionEffect)
    }
}

class DefenseDownEffect(context: Context, duration: Double, isDispel: Boolean, value: Double) :
        NegativeEffect(EffectDefDown(context), duration, isDispel) {
    private val actionEffect = object : InstantActionEffect() {
        override fun put(holder: Unit, initiator: Unit) {
            holder.def -= value
        }

        override fun remove(holder: Unit, initiator: Unit) {
            holder.def += value
        }
    }

    init {
        actionEffects.add(actionEffect)
    }
}

class WoundEffect(private var context: Context, duration: Double, val dotMultiple: Double, override var maxCount: Int) :
        DOT(EffectWound(context), duration, false) {
    private val actionEffect = object : PeriodicActionEffect() {
        override fun tick(count: Int, holder: Unit, initiator: Unit) {
            initiator.damageEvent(initiator.damage * dotMultiple * count, holder, this@WoundEffect)
        }
    }

    init {
        actionEffects.add(actionEffect)
    }

    override fun copy(): Effect {
        return WoundEffect(context, duration, dotMultiple, maxCount)
    }
}

class PoisingEffect(private var context: Context, duration: Double, val dotMultiple: Double, override var maxCount: Int) :
        DOT(EffectPoison(context), duration, true) {
    private val actionEffect = object : PeriodicActionEffect() {
        override fun tick(count: Int, holder: Unit, initiator: Unit) {
            initiator.damageEvent(initiator.damage * dotMultiple * count, holder, this@PoisingEffect)
        }
    }

    init {
        actionEffects.add(actionEffect)
    }

    override fun copy(): Effect {
        return PoisingEffect(context, duration, dotMultiple, maxCount)
    }
}

class ManaBurnEffect(private var context: Context, duration: Double, val burnValue: Double, val mDotDamage: Double, override var maxCount: Int) :
        DOT(EffectManaBurn(context), duration, true) {
    private val actionEffect = object : PeriodicActionEffect() {
        override fun tick(count: Int, holder: Unit, initiator: Unit) {
            holder.mana -= burnValue
            initiator.damageEvent(initiator.damage * mDotDamage, holder, this@ManaBurnEffect)
            initiator.energy += burnValue
        }
    }

    init {
        actionEffects.add(actionEffect)
    }

    override fun copy(): Effect {
        return ManaBurnEffect(context, duration, burnValue, mDotDamage, maxCount)
    }
}

class HealingEffect(context: Context, duration: Double, hotMultiple: Double, override var maxCount: Int) :
        HOT(EffectHealing(context), duration, true) {
    private val actionEffect = object : PeriodicActionEffect() {
        override fun tick(count: Int, holder: Unit, initiator: Unit) {
            holder.healSelfEvent(holder.damage * hotMultiple * count, this@HealingEffect)
        }
    }

    init {
        actionEffects.add(actionEffect)
    }
}

