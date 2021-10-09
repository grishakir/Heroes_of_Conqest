package com.thekingames.medivalwarriors2.core.controller.ai

import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.model.interfaces.GameCycle


class BehaviorTree : GameCycle {
    var actionType: ActionType = ActionType.NONE
        get() {
            return behavior.getActionType()
        }
    lateinit var unit: Unit
    private lateinit var target: Unit
    lateinit var behavior: Behavior

    fun build(unit: Unit) {
        this.unit = unit
        this.target = unit.focus
        when (unit) {
            is Warrior -> behavior = build(unit)
            is Hunter -> behavior = build(unit)
            is Mage -> behavior = build(unit)
            is Rogue -> behavior = build(unit)
            is Paladin -> behavior = build(unit)
            is Berserker -> behavior = build(unit)
        }
        behavior.startListening()
    }

    override fun begin() {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun end() {
        behavior.stopListening()
    }

    private fun build(unit: Warrior): Behavior {
        return WarriorBehavior(target, unit)
    }

    private fun build(unit: Hunter): Behavior {
        return HunterBehavior(target, unit)
    }

    private fun build(unit: Mage): Behavior {
        return MageBehavior(target, unit)
    }

    fun build(unit: Rogue): Behavior {
        return RogueBehavior(target, unit)
    }

    fun build(unit: Paladin): Behavior {

        return PaladinBehavior(target, unit)
    }

    fun build(unit: Berserker): Behavior {
        return BerserkBehavior(target, unit)
    }
}