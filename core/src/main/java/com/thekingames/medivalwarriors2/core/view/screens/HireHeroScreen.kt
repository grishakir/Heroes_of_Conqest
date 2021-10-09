package com.thekingames.medivalwarriors2.core.view.screens

import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.view.activity.HEROES
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.screenmanager.Screen
import kotlinx.android.synthetic.main.a8_hire_hero.view.*

const val M = 200

class HireHeroScreen(parent: ViewGroup) : Screen(parent, R.layout.a8_hire_hero) {
    var index: Int = 0
    val a = activity as MainActivity
    var price = M * a.player.heroes.size
    val unitsIcons: Array<ImageView> = arrayOf(view.icon_warrior,
            view.icon_hunter,
            view.icon_mage,
            view.icon_rogue,
            view.icon_paladin,
            view.icon_berserk)

    override fun releaseDate() {
        a.setHeader(R.string.b_search_for_heroes)
        val unitSelector = view.findViewById<LinearLayout>(R.id.unit_selector)
        val buy = view.findViewById<Button>(R.id.buy)


        unitsIcons.forEach {
            it.setOnClickListener({
                index = unitSelector.indexOfChild(it)
                activate(unitsIcons[index])
            })
        }

        buy.isEnabled = price <= a.player.crystals

        activate(unitsIcons[index])
        buy.text = (price.toString())
        buy.setOnClickListener({
            if (a.player.crystals >= price) {
                val hero = createHero(index)
                hero.load(a)
                a.player.heroes.add(hero)
                a.player.crystals -= price
                price = M * a.player.heroes.size
                if (a.player.crystals >= price) {
                    a.new(HEROES)
                }
                buy.text = (price.toString())
                buy.isEnabled = price <= a.player.crystals

            }
        })
        view.close_dialog.setOnClickListener({ a.onBackPressed() })
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

    private fun createHero(index: Int): Unit {
        return when (index) {
            0 -> Warrior()
            1 -> Hunter()
            2 -> Mage()
            3 -> Rogue()
            4 -> Paladin()
            5 -> Berserker()
            else -> {
                Warrior()
            }
        }
    }
}