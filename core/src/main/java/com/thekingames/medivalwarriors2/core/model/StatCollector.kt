package com.thekingames.medivalwarriors2.core.model

import android.text.format.DateFormat
import com.thekingames.medivalwarriors2.core.model.interfaces.OnDamageListener
import com.thekingames.medivalwarriors2.core.model.interfaces.OnHealListener
import com.thekingames.medivalwarriors2.core.model.interfaces.OnSpellUseListener
import com.thekingames.medivalwarriors2.core.model.interfaces.OnTickListener
import java.util.*

class StatCollector(val units: ArrayList<Unit>) : OnTickListener {

    var isVictory = false

    override fun onTick() {
        timeS += 0.1
    }

    var timeS: Double = 0.0

    var heal: Double = 0.0
        private set(value) {
            field = value
        }

    var attackDamage: Double = 0.0
        private set(value) {
            field = value
        }
    var spellDamage: Double = 0.0
        private set(value) {
            field = value
        }
    var effectDamage: Double = 0.0
        private set(value) {
            field = value
        }
    var damage = 0.0
        get() {
            return attackDamage + spellDamage + effectDamage
        }
    var maxDamage: Double = 0.0
        private set(value) {
            if (value > field) {
                field = value
            }
        }

    var maxHeal: Double = 0.0
        private set(value) {
            if (value > field) {
                field = value
            }
        }
    var spellHistory: ArrayList<Spell> = arrayListOf()
    var timeHistory: ArrayList<Double> = arrayListOf()
    var damageHistory: ArrayList<String> = arrayListOf()


    init {
        setListeners()
    }

    private fun setListeners() {
        units.forEach {
            it.onDamageListeners.add(object : OnDamageListener {
                override fun makeDamage(damage: Double, effect: Effect) {
                    effectDamage += damage
                    maxDamage = damage
                }

                override fun makeDamage(damage: Double, spell: SpellDamage) {
                    spellDamage += damage
                    maxDamage = damage
                    spellHistory.add(spell as Spell)
                    timeHistory.add(timeS)
                    damageHistory.add(damage.toInt().toString())
                }
            })
        }

        //own heal
        units.forEach {
            it.onHealListeners.add(object : OnHealListener {
                override fun makeHeal(heal: Double) {
                    this@StatCollector.heal += heal
                    maxHeal = heal
                }

                override fun makeHeal(heal: Double, effect: Effect) {
                    this@StatCollector.heal += heal
                    maxHeal = heal
                }

                override fun makeHeal(heal: Double, spell: SpellHeal) {
                    this@StatCollector.heal += heal
                    maxHeal = heal
                    spellHistory.add(spell as Spell)
                    timeHistory.add(timeS)
                    val text = heal.toInt()
                    damageHistory.add("+$text")
                }
            }
            )
        }
        units.forEach { it.onSpellUseListeners.add(OnSpellUseListener { }) }
    }

    fun getTime(): String {
        val date = Date()
        date.time = (timeS * 1000).toLong()
        return DateFormat.format("mm:ss", date).toString()
    }

    fun aDamage(): Double {
        return damage / (timeS.toInt())
    }
}
