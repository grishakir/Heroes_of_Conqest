package com.thekingames.medivalwarriors2.core.controller

import android.view.View
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.model.interfaces.GameCycle
import com.thekingames.medivalwarriors2.core.model.interfaces.OnTickListener

abstract class Controller : GameCycle, OnTickListener {
    lateinit var unit: Unit
    private var uiEnable: Boolean = true
    private var onClickListenerArray: ArrayList<View.OnClickListener> = arrayListOf()

    fun addOnClickListeners() {
        for (i in 0 until unit.spells.size) {
            onClickListenerArray.add(View.OnClickListener {
                val callDown = unit.spells[i].callDown
                val view = it
                val index = unit.spells.indexOfFirst { it.view.getSkillIcon() == view }
                if (unit.spells[index].canUse(unit) && uiEnable) {
                    if (unit.spells[index].touch(callDown * unit.localCallDown)) {
                        unit.spells[index].use(unit.target, unit)
                        for (j in 0 until unit.spells.size) {
                            unit.spells[j].globalCallDown(unit.gcd)
                        }
                    }
                }
            })
        }
    }

    override fun begin() {
        uiEnable = true
    }

    override fun pause() {
        uiEnable = false
    }

    override fun resume() {
        uiEnable = true
    }

    override fun end() {
        unit.spells.forEach { it.view.setOnClickListener { } }
    }

    fun setOnClickListeners() {
        for (i in 0 until unit.spells.size) {
            unit.spells[i].view.getSkillIcon().setOnClickListener(onClickListenerArray[i])
        }
    }


    fun skillSelector(index: Int) {
        onClickListenerArray[index].onClick(unit.spells[index].view.getSkillIcon())
    }

    protected fun skill1() {
        skillSelector(0)
    }

    protected fun skill2() {
        skillSelector(1)
    }

    protected fun skill3() {
        skillSelector(2)
    }

    protected fun skill4() {
        skillSelector(3)
    }

    protected fun skill5() {
        skillSelector(4)
    }
}