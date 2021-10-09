package com.thekingames.medivalwarriors2.core.controller.ai

import android.view.View
import com.thekingames.medivalwarriors2.core.controller.Controller

/**
 * The class of AI controller
 * @see Behavior
 */
open class AIController : Controller() {
        private val aiTree = BehaviorTree()

    override fun begin() {
        super.begin()
        addOnClickListeners()
        aiTree.build(unit)
        unit.spells.forEach { it.view.visibility = View.GONE }
    }

    override fun onTick() {
        if (unit.focus == unit.target) {
            when (aiTree.actionType) {
                ActionType.NONE -> {
                    return
                }
                ActionType.SPELL1,
                ActionType.SPELL2,
                ActionType.SPELL3,
                ActionType.SPELL4,
                ActionType.SPELL5 -> {
                    skillSelector(aiTree.actionType.ordinal)
                }
                ActionType.SPAM_ROTATION -> {
                    unit.spellsRotation.forEach {
                        if (it.rotationCondition) {
                            skillSelector(unit.spells.indexOf(it))
                        }
                    }
                }
            }
        }
    }

    override fun end() {

    }
}