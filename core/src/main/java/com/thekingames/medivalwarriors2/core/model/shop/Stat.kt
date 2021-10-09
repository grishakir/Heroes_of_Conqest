package com.thekingames.medivalwarriors2.core.model.shop

import android.content.Context
import com.thekingames.items.R
import com.thekingames.medivalwarriors2.core.model.Unit
import java.io.Serializable

abstract class Stat(var value: Int) : Serializable {

    open fun getDescription(context: Context): String {
        return ""
    }

    abstract var modifier: Modifier
}

class StatS(value: Int) : Stat(value) {
    override fun getDescription(context: Context): String {
        return context.getString(R.string.stat_s, value.toString())
    }

    override var modifier: Modifier = object : Modifier {
        override fun apply(unit: Unit) {
            unit.force += value
        }

        override fun cancel(unit: Unit) {
            unit.force -= value
        }
    }
}

class StatA(value: Int) : Stat(value) {

    override fun getDescription(context: Context): String {
        return context.getString(R.string.stat_a, value.toString())
    }

    override var modifier: Modifier = object : Modifier {
        override fun apply(unit: Unit) {
            unit.agility += value
        }

        override fun cancel(unit: Unit) {
            unit.agility -= value
        }
    }
}

class StatE(value: Int) : Stat(value) {

    override fun getDescription(context: Context): String {
        return context.getString(R.string.stat_e, value.toString())
    }

    override var modifier: Modifier = object : Modifier {
        override fun apply(unit: Unit) {
            unit.stamina += value
        }

        override fun cancel(unit: Unit) {
            unit.stamina -= value
        }
    }
}

class StatM(value: Int) : Stat(value) {

    override fun getDescription(context: Context): String {
        return context.getString(R.string.stat_m, value.toString())
    }

    override var modifier: Modifier = object : Modifier {
        override fun apply(unit: Unit) {
            unit.mind += value
        }

        override fun cancel(unit: Unit) {
            unit.mind -= value
        }
    }
}

class StatDmg(value: Int) : Stat(value) {

    override fun getDescription(context: Context): String {
        return context.getString(R.string.stat_dmg, value.toString())
    }

    override var modifier: Modifier = object : Modifier {
        override fun apply(unit: Unit) {
            unit.baseDamage += value
        }

        override fun cancel(unit: Unit) {
            unit.baseDamage -= value
        }
    }

}