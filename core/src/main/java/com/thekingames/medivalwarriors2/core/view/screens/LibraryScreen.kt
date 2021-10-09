package com.thekingames.medivalwarriors2.core.view.screens

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.screenmanager.Screen
import kotlinx.android.synthetic.main.a6_library.view.*

class LibraryScreen(parent: ViewGroup) : Screen(parent, R.layout.a6_library) {
    //region ui obj a3_heroes
    lateinit var warrior: Unit
    lateinit var hunter: Unit
    lateinit var mage: Unit
    lateinit var rogue: Unit
    lateinit var paladin: Unit
    lateinit var berserk: Unit
    lateinit var heroes: Array<Unit>
    lateinit var descriptions: Array<String>

    val unitsIcons: Array<Int> = arrayOf(R.drawable.icon_warrior,
            R.drawable.icon_hunter,
            R.drawable.icon_mage,
            R.drawable.icon_rogue,
            R.drawable.icon_paladin,
            R.drawable.icon_berserk)
    //endregion
    val a = activity as MainActivity
    var chooseHero: Unit? = null
    lateinit var t1: Spell
    lateinit var t2: Spell
    lateinit var t3: Spell

    private fun unActivateAll() {
        for (i in 0 until view.unit_selector.childCount) {
            (view.unit_selector.getChildAt(i) as ImageView).setBackgroundResource(R.drawable.fon_icon1)
        }
        isTalentsShowing = false
    }

    private fun activate(imageView: ImageView) {
        unActivateAll()
        imageView.setBackgroundResource(R.drawable.fon_icon2)
    }

    private fun showWarrior() {
        val cardParent = view.hero_description
        cardParent.removeAllViewsInLayout()
        CardWarrior(cardParent).show()
        warrior.spells.forEach { SpellCardView(it, cardParent).show() }
        activate(view.icon_warrior)
    }

    private fun showHunter() {
        val cardParent = view.hero_description
        cardParent.removeAllViewsInLayout()
        CardHunter(cardParent).show()
        hunter.spells.forEach { SpellCardView(it, cardParent).show() }
        activate(view.icon_hunter)
    }

    private fun showMage() {
        val cardParent = view.hero_description
        cardParent.removeAllViewsInLayout()
        CardMage(cardParent).show()
        mage.spells.forEach { SpellCardView(it, cardParent).show() }
        activate(view.icon_mage)
    }

    private fun showRogue() {
        val cardParent = view.hero_description
        cardParent.removeAllViewsInLayout()
        CardRogue(cardParent).show()
        rogue.spells.forEach { SpellCardView(it, cardParent).show() }
        activate(view.icon_rogue)
    }

    private fun showPaladin() {
        val cardParent = view.hero_description
        cardParent.removeAllViewsInLayout()
        CardPaladin(cardParent).show()
        paladin.spells.forEach { SpellCardView(it, cardParent).show() }
        activate(view.icon_paladin)
    }

    private fun showBerserk() {
        val cardParent = view.hero_description
        cardParent.removeAllViewsInLayout()
        CardBerserk(cardParent).show()
        berserk.spells.forEach { SpellCardView(it, cardParent).show() }
        activate(view.icon_berserk)
    }

    private fun showTalents() {
        val cardParent = view.hero_description
        cardParent.removeAllViewsInLayout()
        SpellCardView(t1, cardParent).show()
        SpellCardView(t2, cardParent).show()
        SpellCardView(t3, cardParent).show()
        activate(view.icon_talents)
    }

    private var isTalentsShowing: Boolean = false

    override fun settingsView() {
        super.settingsView()
        warrior = Warrior()
        hunter = Hunter()
        mage = Mage()
        rogue = Rogue()
        paladin = Paladin()
        berserk = Berserker()
        heroes = arrayOf(warrior, hunter, mage, rogue, paladin, berserk)
        descriptions = activity.resources.getStringArray(R.array.heroes_descriptions)
        view.text_showing_button.setOnClickListener {
            if (view.text_hero.visibility == View.GONE) {
                view.text_hero.visibility = View.VISIBLE
            } else {
                view.text_hero.visibility = View.GONE
            }
        }
        warrior.load(activity)

        hunter.load(activity)

        mage.load(activity)

        rogue.load(activity)

        paladin.load(activity)

        berserk.load(activity)

        chooseHero = warrior
        showWarrior()
        view.text_hero.text = descriptions[heroes.indexOf(chooseHero)]

        t1 = SpellT1Freedom()
        t2 = SpellT1DispelMagic()
        t3 = SpellT1MagicShield()

        arrayOf(t1, t2, t3).forEach { it.load(activity) }

        view.icon_warrior.setOnClickListener {
            if (chooseHero != warrior) {
                chooseHero = warrior
                showWarrior()
                view.text_hero.text = descriptions[heroes.indexOf(chooseHero)]
            }
        }
        view.icon_hunter.setOnClickListener {
            if (chooseHero != hunter) {
                chooseHero = hunter
                showHunter()
                view.text_hero.text = descriptions[heroes.indexOf(chooseHero)]
            }
        }
        view.icon_mage.setOnClickListener {
            if (chooseHero != mage) {
                chooseHero = mage
                showMage()
                view.text_hero.text = descriptions[heroes.indexOf(chooseHero)]
            }
        }
        view.icon_rogue.setOnClickListener {
            if (chooseHero != rogue) {
                chooseHero = rogue
                showRogue()
            }
        }
        view.icon_paladin.setOnClickListener {
            if (chooseHero != paladin) {
                chooseHero = paladin
                showPaladin()
                view.text_hero.text = descriptions[heroes.indexOf(chooseHero)]
            }
        }
        view.icon_berserk.setOnClickListener {
            if (chooseHero != berserk) {
                chooseHero = berserk
                showBerserk()
                view.text_hero.text = descriptions[heroes.indexOf(chooseHero)]
            }
        }
        view.icon_talents.setOnClickListener {
            if (!isTalentsShowing) {
                chooseHero = null
                isTalentsShowing = true
                showTalents()
                val talentsDescIndex = 6
                view.text_hero.text = descriptions[talentsDescIndex]
                view.text_hero.visibility = View.GONE
            }
        }
    }

    override fun releaseDate() {
        super.releaseDate()
        a.setHeader(R.string.b_library)
    }
}
