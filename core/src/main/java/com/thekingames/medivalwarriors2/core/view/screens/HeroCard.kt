package com.thekingames.medivalwarriors2.core.view.screens

import android.view.ViewGroup
import android.widget.TextView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.view.activity.Settings.CHEAT_ENABLE
import com.thekingames.screenmanager.Fragment
import kotlinx.android.synthetic.main.c_hero.view.*

abstract class HeroCardView(parent: ViewGroup) : Fragment(parent, R.layout.c_hero) {

    abstract fun getUnit(): Unit

    override fun settingsView() {
        super.settingsView()
        view.image_hero.setImageResource(R.drawable.preview_mage)
        releaseCard()
        if (CHEAT_ENABLE) {
            view.setOnClickListener { getUnit().exp += getUnit().capExp / 2 }
        }
    }

    fun releaseCard() {
        val unit = getUnit()
        (view.hero_stats.getChildAt(0) as TextView).text = (unit.maxHp.toInt().toString())
        (view.hero_stats.getChildAt(1) as TextView).text = (unit.baseDamage.toInt().toString())
        (view.hero_stats.getChildAt(2) as TextView).text = ("${(unit.baseRes * 100).toInt()}%")
    }
}

open class CardWarrior(parent: ViewGroup) : HeroCardView(parent) {

    override fun settingsView() {
        super.settingsView()
        view.name.setText(R.string.warrior)
        view.image_hero.setImageResource(R.drawable.preview_warrior)
    }

    override fun getUnit(): Unit {
        return Warrior()
    }
}

open class CardHunter(parent: ViewGroup) : HeroCardView(parent) {
    override fun settingsView() {
        super.settingsView()
        view.name.setText(R.string.hunter)
        view.image_hero.setImageResource(R.drawable.preview_hunter)
    }

    override fun getUnit(): Unit {
        return Hunter()
    }
}

open class CardMage(parent: ViewGroup) : HeroCardView(parent) {

    override fun settingsView() {
        super.settingsView()
        view.name.setText(R.string.mage)
        view.image_hero.setImageResource(R.drawable.preview_mage)
    }

    override fun getUnit(): Unit {
        return Mage()
    }
}

open class CardRogue(parent: ViewGroup) : HeroCardView(parent) {
    override fun settingsView() {
        super.settingsView()
        view.name.setText(R.string.rogue)
        view.image_hero.setImageResource(R.drawable.preview_rogue)
    }

    override fun getUnit(): Unit {
        return Rogue()
    }
}

open class CardPaladin(parent: ViewGroup) : HeroCardView(parent) {
    override fun settingsView() {
        super.settingsView()
        view.name.setText(R.string.paladin)
        view.image_hero.setImageResource(R.drawable.preview_paladin)
    }

    override fun getUnit(): Unit {
        return Paladin()
    }
}

open class CardBerserk(parent: ViewGroup) : HeroCardView(parent) {
    override fun settingsView() {
        super.settingsView()
        view.name.setText(R.string.berserk)
        view.image_hero.setImageResource(R.drawable.preview_berserk)
    }

    override fun getUnit(): Unit {
        return Berserker()
    }
}