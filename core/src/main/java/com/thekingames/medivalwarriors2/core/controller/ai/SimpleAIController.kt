package com.thekingames.medivalwarriors2.core.controller.ai

import android.view.View
import com.thekingames.medivalwarriors2.core.controller.Controller

class SimpleAIController: AIController() {

    override fun begin() {
        addOnClickListeners()
        unit.spells.forEach { it.view.visibility = View.INVISIBLE }
    }

    override fun onTick() {
        unit.spells.forEach {  skillSelector(unit.spells.indexOf(it)) }
    }

    override fun end() {

    }
}