package com.thekingames.medivalwarriors2.core.controller.ai

import android.util.Log
import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit

/**
The class of Behavior of artificial intelligence(AI) is described by a set of used abilities and their priority.
The behavior itself also has priority given dynamically depending on the information in the blackboard.
 *@see BehaviorTree
 */

private const val DIMINISHING_DURATION = 20.0

abstract class Behavior(var target: Unit, var unit: Unit) {

    abstract var spellDef: ActionType
    abstract var spellBurst: ActionType
    abstract var spellCC: ActionType


    fun <T> find(t: T?): Boolean {
        return t != null
    }

    open fun getActionType(): ActionType {
        if (defConditionBurstAndDuration() && def()) {
            Log.i("AI_ACTION", "DEF")
            return spellDef
        } else if (ccConditionBurst() && ccConditionSave() && ccConditionFullCC() && cc() || (
                        find(target.effects.firstOrNull { it is BurstEffect })) && cc()) {
            Log.i("AI_ACTION", "CC")
            return spellCC
        } else if (burstConditionCCDrop() && burstConditionDefDrop() && burst() || (
                        find(target.effects.firstOrNull { it is StunEffect })) && burst()) {
            Log.i("AI_ACTION", "BURST")
            return spellBurst
        } else if (rotationCondition()) {
            Log.i("AI_ACTION", "ROTATION")
            return ActionType.SPAM_ROTATION
        }
        Log.i("AI_ACTION", "NONE")
        return ActionType.NONE
    }

    open fun startListening() {

    }

    open fun stopListening() {

    }

    abstract fun burst(): Boolean
    abstract fun def(): Boolean
    abstract fun cc(): Boolean

    //region burst

    protected open fun burstConditionCCDrop(): Boolean {
        return find(target.spellsCC.firstOrNull { !it.isReady }) ||
                target.spellsCC.isEmpty()
    }

    protected open fun burstConditionDefDrop(): Boolean {
        return find(target.spellsDefence.firstOrNull { !it.isReady }) ||
                target.spellsDefence.isEmpty()
    }

    //endregion

    //region control
    protected open fun ccConditionFullCC(): Boolean {
        return target.isControl && target.stunDiminishing.value == 1.0
    }

    protected open fun ccConditionSave(): Boolean {
        return find(target.spellsBurst.firstOrNull { (it).rest > DIMINISHING_DURATION }) ||
                target.spellsBurst.isEmpty()
    }

    protected open fun ccConditionBurst(): Boolean {
        return find(unit.spellsBurst.firstOrNull { (it).rest > DIMINISHING_DURATION }) ||
                find(unit.spellsBurst.firstOrNull { it.isReady }) ||
                unit.spellsBurst.isEmpty()
    }

    //endregion

    //region def
    protected open fun defConditionBurstAndDuration(): Boolean {
        val burstEffect = target.effects.firstOrNull { (it is BurstEffect) }
        return target.spellsBurst.isEmpty() ||
                (burstEffect != null &&
                        (burstEffect).rest >= 5.0 &&
                        unit.spellsDefence.isNotEmpty())

    }
    //endregion

    open fun rotationCondition(): Boolean {
        return true
    }

    protected fun useSpell(spell: Spell): Boolean {
        return spell.isReady
    }
}

class WarriorBehavior(target: Unit, unit: Unit) : Behavior(target, unit) {

    override var spellDef: ActionType = ActionType.SPELL2
    override var spellBurst: ActionType = ActionType.SPELL5
    override var spellCC: ActionType = ActionType.SPELL4

    override fun rotationCondition(): Boolean {
        unit.spells[SPELL_1].rotationCondition = unit.rage >= 85
        unit.spells[SPELL_3].rotationCondition = unit.rage >= 40
        return true
    }

    override fun burst(): Boolean {
        return useSpell(unit.spells[SPELL_5])
    }

    override fun def(): Boolean {
        return useSpell(unit.spells[SPELL_2])
    }


    override fun cc(): Boolean {
        return useSpell(unit.spells[SPELL_4])
    }
}

class HunterBehavior(target: Unit, unit: Unit) : Behavior(target, unit) {

    override var spellDef: ActionType = ActionType.SPELL5
    override var spellBurst: ActionType = ActionType.SPELL3
    override var spellCC: ActionType = ActionType.SPELL2

    override fun rotationCondition(): Boolean {
        unit.spells[SPELL_1].rotationCondition = unit.mana >= 55
        unit.spells[SPELL_4].rotationCondition = unit.energy >= 70
        return true
    }

    override fun burst(): Boolean {
        return useSpell(unit.spells[SPELL_3]) && unit.energy >= 60
    }

    override fun def(): Boolean {
        return useSpell(unit.spells[SPELL_5])
    }

    override fun cc(): Boolean {
        return useSpell(unit.spells[SPELL_2])
    }
}

class MageBehavior(target: Unit, unit: Unit) : Behavior(target, unit) {

    override var spellDef: ActionType = ActionType.SPELL4
    override var spellBurst: ActionType = ActionType.NONE
    override var spellCC: ActionType = ActionType.SPELL3

    override fun rotationCondition(): Boolean {
        unit.spells[SPELL_1].rotationCondition = unit.mana >= unit.spells[SPELL_4].cost
        fun spellStackCondition(): Boolean {
            val effect = unit.effects.firstOrNull { it is WitchcraftEffect }
            return if (effect != null) {
                effect.currentCount >= 3
            } else {
                false
            }
        }
        unit.spells[SPELL_2].rotationCondition = spellStackCondition()
        unit.spells[SPELL_5].rotationCondition = spellStackCondition()
        return true
    }

    override fun burst(): Boolean {
        return false
    }

    override fun def(): Boolean {
        return useSpell(unit.spells[SPELL_4])
    }

    override fun cc(): Boolean {
        return useSpell(unit.spells[SPELL_3])
    }
}

class RogueBehavior(target: Unit, unit: Unit) : Behavior(target, unit) {

    override var spellDef: ActionType = ActionType.SPELL3
    override var spellBurst: ActionType = ActionType.SPELL5
    override var spellCC: ActionType = ActionType.SPELL3

    override fun rotationCondition(): Boolean {
        val costCombo = unit.spells[SPELL_4].cost + unit.spells[SPELL_2].cost
        unit.spells[SPELL_1].rotationCondition = (unit.energy >= costCombo && !unit.spells[SPELL_2].isReady) || (!unit.spells[SPELL_4].isReady && find(unit.focus.effects.find { it is DefenseDownEffect }))
        unit.spells[SPELL_2].rotationCondition = (unit.energy >= (costCombo - unit.gcd*unit.rEnergy)) &&
                (unit.spells[SPELL_4].rest <= unit.gcd)
        unit.spells[SPELL_4].rotationCondition = find(target.effects.firstOrNull { it is DefenseDownEffect })
        return true
    }

    override fun burst(): Boolean {
        return useSpell(unit.spells[SPELL_5])
    }

    override fun def(): Boolean {
        return useSpell(unit.spells[SPELL_3])
    }

    override fun cc(): Boolean {
        return useSpell(unit.spells[SPELL_3])
    }
}

class PaladinBehavior(target: Unit, unit: Unit) : Behavior(target, unit) {

    override var spellDef: ActionType = ActionType.NONE
    override var spellBurst: ActionType = ActionType.SPELL3
    override var spellCC: ActionType = ActionType.SPELL4

    override fun rotationCondition(): Boolean {
        unit.spells[SPELL_1].rotationCondition = unit.rage >= unit.spells[SPELL_4].cost
        unit.spells[SPELL_2].rotationCondition = unit.mana >= 40 || find(unit.effects.find { it is BurstEffect })
        unit.spells[SPELL_5].rotationCondition = (unit.hp <= unit.maxHp * 0.40) && (unit.mana >= unit.spells[SPELL_5].cost) && (unit.multipleHealing >= 0.5)
        return true
    }

    override fun burst(): Boolean {
        return useSpell(unit.spells[SPELL_3])
    }

    override fun def(): Boolean {
        return false
    }

    override fun cc(): Boolean {
        return useSpell(unit.spells[SPELL_4])
    }
}

class BerserkBehavior(target: Unit, unit: Unit) : Behavior(target, unit) {

    override var spellDef: ActionType = ActionType.NONE
    override var spellBurst: ActionType = ActionType.SPELL1
    override var spellCC: ActionType = ActionType.SPELL3

    override fun rotationCondition(): Boolean {
        unit.spells[SPELL_2].rotationCondition = unit.energy >= 90
        unit.spells[SPELL_4].rotationCondition = ((unit.rage >= 60 && unit.spells[SPELL_5].rest <= unit.gcd) && !unit.spells[SPELL_1].isReady) ||
                ((unit.rage >= 60 && unit.spells[SPELL_5].rest <= unit.gcd) && !unit.spells[SPELL_1].canUse(unit)) ||
                find(unit.effects.firstOrNull { it is BurstEffect })
        unit.spells[SPELL_5].rotationCondition = find(target.effects.firstOrNull { it is DefenseDownEffect }) ||
                (unit.energy >= 70 && unit.spells[SPELL_1].isReady)
        return true
    }

    override fun burst(): Boolean {
        return useSpell(unit.spells[SPELL_1]) && unit.rage >= 10.0
    }

    override fun def(): Boolean {
        return false
    }

    override fun cc(): Boolean {
        return useSpell(unit.spells[SPELL_3])
    }
}
