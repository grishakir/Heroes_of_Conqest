package com.thekingames.medivalwarriors2.core.model.shop

import android.content.Context
import android.view.ViewGroup
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.SpellHpFlask
import com.thekingames.medivalwarriors2.core.model.SpellManaFlask
import com.thekingames.medivalwarriors2.core.model.Unit
import java.io.Serializable

const val TWO_HANDED = 0
const val LEFT_HAND = 1
const val RIGHT_HAND = 2
const val ONE_HANDED = 3
const val SUPPORT_SLOT = 4
const val NONE = 5


open class Item(var lvl: Int,
                var quality: Int,
                var classSelector: ClassSelector,
                var cost: Int,
                var idIcon: Int,
                var idName: Int,
                var type: Int
) : Serializable {


    val stats: ArrayList<Stat> = arrayListOf()

    var count: Int = 1

    fun apply(unit: Unit) {
        stats.forEach { it.modifier.apply(unit) }
    }

    fun cancel(unit: Unit) {
        stats.forEach { it.modifier.cancel(unit) }
    }

    fun copy(): Item {
        return Item(lvl, quality, classSelector, cost, idIcon, idName, type)
    }

}

class WarriorItem(lvl: Int,
                  quality: Int,
                  cost: Int,
                  idIcon: Int,
                  idName: Int,
                  type: Int) : Item(lvl, quality, ClassSelector.Warrior, cost, idIcon, idName, type) {
}

class HunterItem(lvl: Int,
                 quality: Int,
                 cost: Int,
                 idIcon: Int,
                 idName: Int,
                 type: Int) : Item(lvl, quality, ClassSelector.Hunter, cost, idIcon, idName, type) {

}

class MageItem(lvl: Int,
               quality: Int,
               cost: Int,
               idIcon: Int,
               idName: Int,
               type: Int) : Item(lvl, quality, ClassSelector.Mage, cost, idIcon, idName, type) {

}

class RogueItem(lvl: Int,
                quality: Int,
                cost: Int,
                idIcon: Int,
                idName: Int,
                type: Int) : Item(lvl, quality, ClassSelector.Rogue, cost, idIcon, idName, type) {

}

class PaladinItem(lvl: Int,
                  quality: Int,
                  cost: Int,
                  idIcon: Int,
                  idName: Int,
                  type: Int) : Item(lvl, quality, ClassSelector.Paladin, cost, idIcon, idName, type) {

}

class BerserkItem(lvl: Int,
                  quality: Int,
                  cost: Int,
                  idIcon: Int,
                  idName: Int,
                  type: Int) : Item(lvl, quality, ClassSelector.Berserk, cost, idIcon, idName, type) {
}

class FlaskHpItem : Item(1, 3, ClassSelector.NONE, 5000, R.drawable.flask_hp, R.string.flask_hp_name, SUPPORT_SLOT), Serializable {

    init {
        count = 5
        stats.add(object : Stat(10) {
            override fun getDescription(context: Context): String {
                return context.getString(R.string.stat_res_heal)
            }

            override var modifier: Modifier = object : Modifier {
                override fun apply(unit: Unit) {
                    val spell = SpellHpFlask()
                    unit.spells.add(spell)
                    spell.count = count
                }

                override fun cancel(unit: Unit) {
                    val spell = unit.spells.first { it is SpellHpFlask }
                    (spell.view.parent as ViewGroup?)?.removeView(spell.view)
                    unit.spells.remove(spell)
                }
            }
        })
    }
}

class FlaskManaItem : Item(1, 3, ClassSelector.NONE, 2500, R.drawable.flask_mana, R.string.flask_mana_name, SUPPORT_SLOT), Serializable {

    init {
        count = 5
        stats.add(object : Stat(70) {
            override fun getDescription(context: Context): String {
                return context.getString(R.string.stat_res_mana)
            }

            override var modifier: Modifier = object : Modifier {
                override fun apply(unit: Unit) {
                    val spell = SpellManaFlask()
                    unit.spells.add(spell)
                    spell.count = count
                }

                override fun cancel(unit: Unit) {
                    val spell = unit.spells.first { it is SpellManaFlask }
                    (spell.view.parent as ViewGroup?)?.removeView(spell.view)
                    unit.spells.remove(spell)
                }
            }
        })
    }
}

class ToolsItem : Item(1, 0, ClassSelector.NONE, 40, R.drawable.tools, R.string.tools_name, NONE) {

    init {
        stats.add(object : Stat(0) {
            override fun getDescription(context: Context): String {
                return context.getString(R.string.stat_gold)
            }

            override var modifier: Modifier = object : Modifier {
                override fun apply(unit: Unit) {

                }

                override fun cancel(unit: Unit) {

                }
            }
        })
    }
}

class ScrollsItem : Item(5, 6, ClassSelector.NONE, 5000, R.drawable.scroll, R.string.scrolls_name, SUPPORT_SLOT) {
    init {
        stats.add(object : Stat(420) {
            override fun getDescription(context: Context): String {
                return context.getString(R.string.stat_exp)
            }

            override var modifier: Modifier = object : Modifier {
                override fun apply(unit: Unit) {
                    unit.exp += value
                }

                override fun cancel(unit: Unit) {

                }
            }
        })
    }
}