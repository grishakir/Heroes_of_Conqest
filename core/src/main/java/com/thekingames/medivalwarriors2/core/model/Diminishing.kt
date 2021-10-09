package com.thekingames.medivalwarriors2.core.model

import com.thekingames.medivalwarriors2.core.model.interfaces.OnTickListener


class Diminishing : OnTickListener {
    private val duration = 18.0
    var value = 1.0
    private var rest: Double = duration
        set(value) {
            field = if (rest <= 0.0) {
                release()
            } else {
                value
            }
        }

    private fun release(): Double {
        this.value = 1.0
        return 0.0
    }

    override fun onTick() {
        this.rest -= 0.1
    }

    fun start(dumpValue: Double) {
        this.value = this.value * dumpValue
        this.rest = duration
    }
}