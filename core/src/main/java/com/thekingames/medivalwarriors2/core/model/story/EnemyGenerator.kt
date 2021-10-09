package com.thekingames.medivalwarriors2.core.model.story

import com.thekingames.medivalwarriors2.core.model.Unit


class EnemyGenerator {

    fun settingsEasyUnit(lvl: Int, unit: Unit) {
        unit.baseDamage -= unit.baseDamage * 0.75
        unit.stamina -= (unit.stamina * 0.75).toInt()
        unit.stamina += (lvl * 0.3).toInt()
        unit.force += (lvl * 0.3).toInt()
        unit.agility += (lvl * 0.3).toInt()
        unit.mind += (lvl * 0.3).toInt()
    }

    fun settingsEasyBoss(lvl: Int, unit: Unit) {
        unit.baseDamage -= unit.baseDamage * 0.75
        unit.stamina -= (unit.stamina * 0.75).toInt()
        unit.stamina += 10
        unit.stamina += (lvl * 0.3).toInt()
        unit.force += (lvl * 0.3).toInt()
        unit.agility += (lvl * 0.3).toInt()
        unit.mind += (lvl * 0.3).toInt()
    }

    fun settingsHardUnit(lvl: Int, unit: Unit) {
        unit.baseDamage -= unit.baseDamage * 0.65
        unit.stamina -= (unit.stamina * 0.65).toInt()
        unit.stamina += (lvl * 0.4).toInt()
        unit.agility += (lvl * 0.4).toInt()
        unit.force += (lvl * 0.4).toInt()
    }

    fun settingsHardBoss(lvl: Int, unit: Unit) {
        unit.baseDamage -= unit.baseDamage * 0.65
        unit.stamina -= (unit.stamina * 0.65).toInt()
        unit.stamina += 30
        unit.stamina += (lvl * 0.4).toInt()
        unit.agility += (lvl * 0.4).toInt()
        unit.force += (lvl * 0.4).toInt()
        unit.mind += (lvl * 0.4).toInt()
        unit.baseDamage += (lvl * 0.25).toInt()
    }
}