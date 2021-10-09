package com.thekingames.medivalwarriors2.core.view.screens

import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.screenmanager.Screen
import com.thekingames.ui.SpellView
import kotlinx.android.synthetic.main.choose_hero.view.*


class ChooseHeroScreen(parent: ViewGroup) : Screen(parent, R.layout.choose_hero) {
    val a = activity as MainActivity
    private lateinit var units: ArrayList<Unit>
    private lateinit var talents: Array<Spell>
    private var choseTalentIndex: Int = 0
    private lateinit var choseHeroCard: HeroCardView

    override fun settingsView() {
        super.settingsView()



        view.icon_warrior.setOnClickListener {
            if (a.libraryScreen.chooseHero != a.libraryScreen.warrior) {
                a.libraryScreen.chooseHero = a.libraryScreen.warrior
                showWarrior()
            }
        }
        view.icon_hunter.setOnClickListener {
            if (a.libraryScreen.chooseHero != a.libraryScreen.hunter) {
                a.libraryScreen.chooseHero = a.libraryScreen.hunter
                showHunter()
            }
        }
        view.icon_mage.setOnClickListener {
            if (a.libraryScreen.chooseHero != a.libraryScreen.mage) {
                a.libraryScreen.chooseHero = a.libraryScreen.mage
                showMage()
            }
        }
        view.icon_rogue.setOnClickListener {
            if (a.libraryScreen.chooseHero != a.libraryScreen.rogue) {
                a.libraryScreen.chooseHero = a.libraryScreen.rogue
                showRogue()
            }
        }
        view.icon_paladin.setOnClickListener {
            if (a.libraryScreen.chooseHero != a.libraryScreen.paladin) {
                a.libraryScreen.chooseHero = a.libraryScreen.paladin
                showPaladin()
            }
        }
        view.icon_berserk.setOnClickListener {
            if (a.libraryScreen.chooseHero != a.libraryScreen.berserk) {
                a.libraryScreen.chooseHero = a.libraryScreen.berserk
                showBerserk()
            }
        }
        val next = view.findViewById<Button>(R.id.next)
        next.setOnClickListener {
            view.text_header.text = activity.getString(R.string.player2)
            val unit = choseHeroCard.getUnit()
            unit.load(a)
            units.add(unit)
            unit.spells.add(talents[choseTalentIndex])
            unActivateAll()
            showWarrior()
            a.libraryScreen.chooseHero = a.libraryScreen.warrior
            releaseTalents()
            if (units.size == 2) {
                val game = a.gameScreen as GameScreen
                game.setUnits(arrayListOf(units[0]), arrayListOf(units[1]))
                talents.forEach { view.talents.removeView(it.view) }
                a.setScreen(game)
            }
        }
    }

    private fun releaseTalents() {
        view.talents.removeAllViewsInLayout()
        talents = arrayOf(SpellT1DispelMagic(), SpellT1MagicShield(), SpellT1Freedom())
        talents.forEach { it.load(a) }
        talents.forEach {
            val itTalent = it
            view.talents.addView(it.view)
            it.view.setOnClickListener {
                choseTalentIndex = view.talents.indexOfChild(itTalent.view)
                talents.forEach { it.view.unActivate() }
                (it as SpellView).activate()
            }
        }
        talents[choseTalentIndex].view.activate()
    }

    override fun releaseDate() {
        super.releaseDate()
        releaseTalents()
        choseTalentIndex = 0
        units = arrayListOf()
        view.text_header.text = activity.getString(R.string.player1)
        showWarrior()
    }

    private fun unActivateAll() {
        for (i in 0 until view.unit_selector.childCount) {
            (view.unit_selector.getChildAt(i) as ImageView).setBackgroundResource(R.drawable.fon_icon1)
        }
    }

    private fun activate(imageView: ImageView) {
        unActivateAll()
        imageView.setBackgroundResource(R.drawable.fon_icon2)
    }

    private fun showWarrior() {
        val cardParent = view.hero_description
        cardParent.removeAllViews()
        choseHeroCard = CardWarrior(cardParent)
        choseHeroCard.show()
        activate(view.icon_warrior)
    }

    private fun showHunter() {
        val cardParent = view.hero_description
        cardParent.removeAllViews()
        choseHeroCard = CardHunter(cardParent)
        choseHeroCard.show()
        activate(view.icon_hunter)
    }

    private fun showMage() {
        val cardParent = view.hero_description
        cardParent.removeAllViews()
        choseHeroCard = CardMage(cardParent)
        choseHeroCard.show()
        activate(view.icon_mage)
    }

    private fun showRogue() {
        val cardParent = view.hero_description
        cardParent.removeAllViews()
        choseHeroCard = CardRogue(cardParent)
        choseHeroCard.show()
        activate(view.icon_rogue)
    }

    private fun showPaladin() {
        val cardParent = view.hero_description
        cardParent.removeAllViews()
        choseHeroCard = CardPaladin(cardParent)
        choseHeroCard.show()
        activate(view.icon_paladin)
    }

    private fun showBerserk() {
        val cardParent = view.hero_description
        cardParent.removeAllViews()
        choseHeroCard = CardBerserk(cardParent)
        choseHeroCard.show()
        activate(view.icon_berserk)
    }
}

