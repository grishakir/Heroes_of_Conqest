package com.thekingames.medivalwarriors2.core.model.player

import android.content.Context
import android.text.format.Time
import android.util.Log
import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.model.interfaces.OnChangeListener
import com.thekingames.medivalwarriors2.core.model.shop.*
import java.io.Serializable

class Player : Serializable {

    var inventory: ArrayList<Item> = arrayListOf(ToolsItem())

    var access = 0
        set(value) {
            field = value
            onChangeAccessListener?.change()
        }
    @Transient
    var onChangeAccessListener: OnChangeListener? = null

    var tasksEnable = Array(10) { true }

    var savedDay: Int = 0

    @Transient
    var onLvlChangeListener: OnChangeListener? = null
        set(value) {
            field = value
            heroes[currentHeroIndex].onLvlChangeListener = onLvlChangeListener
        }
    var heroes: ArrayList<Unit> = arrayListOf(Warrior())


    var currentHeroIndex = 0
        set(value) {
            heroes[field].onLvlChangeListener = null
            field = value
            heroes[value].onLvlChangeListener = onLvlChangeListener
        }

    var coins: Int = 250
        set(value) {
            field = value
            onChangeCoinsListener?.change()
        }
    @Transient
    var onChangeCoinsListener: OnChangeListener? = null

    var crystals: Int = 15
        set(value) {
            field = value
            onChangeCrystalsListener?.change()
        }

    @Transient
    var onChangeCrystalsListener: OnChangeListener? = null

    fun getCurrentHero(): Unit {
        return heroes[currentHeroIndex]
    }

    fun load(context: Context) {
        heroes.forEach { it.load(context) }
        val time = Time()
        time.setToNow()
        if (time.yearDay != savedDay) {
            tasksEnable = Array(10) { true }
            savedDay = time.yearDay
            generate()
        }
    }

    var shopItems: ArrayList<Item> = arrayListOf()


    fun generate() {
        shopItems = arrayListOf()

        val warriorWithMaxLvl = heroes.filter { it is Warrior }.maxBy { it.lvl }
        val maxLvlWarrior = warriorWithMaxLvl?.lvl ?: 1

        val hunterWithMaxLvl = heroes.filter { it is Hunter }.maxBy { it.lvl }
        val maxLvlHunter = hunterWithMaxLvl?.lvl ?: 1

        val mageWithMaxLvl = heroes.filter { it is Mage }.maxBy { it.lvl }
        val maxLvlMage = mageWithMaxLvl?.lvl ?: 1

        val rogueMaxLvl = heroes.filter { it is Rogue }.maxBy { it.lvl }
        val maxLvlRogue = rogueMaxLvl?.lvl ?: 1

        val paladinWithMaxLvl = heroes.filter { it is Paladin }.maxBy { it.lvl }
        val maxLvlPaladin = paladinWithMaxLvl?.lvl ?: 1

        val berserkerWithMaxLvl = heroes.filter { it is Berserker }.maxBy { it.lvl }
        val maxLvlBerserker = berserkerWithMaxLvl?.lvl ?: 1

        val generator = ItemGenerator(25, 7)
        //1
        when {
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.warriorTemplate1, maxLvlWarrior))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.warriorTemplate2, maxLvlWarrior))
        }
        //2
        when {
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.hunterTemplate1, maxLvlHunter))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.hunterTemplate2, maxLvlHunter))
        }
        //3
        when {
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.mageTemplate1, maxLvlMage))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.mageTemplate2, maxLvlMage))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.mageTemplate3, maxLvlMage))
        }
        //4
        when {
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.rogueTemplate1, maxLvlRogue))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.rogueTemplate2, maxLvlRogue))
        }
        //5
        when {
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.paladinTemplate1, maxLvlPaladin))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.paladinTemplate2, maxLvlPaladin))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.paladinTemplate3, maxLvlPaladin))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.paladinTemplate4, maxLvlPaladin))
        }
        when {
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.berserkTemplate1, maxLvlBerserker))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.berserkTemplate2, maxLvlBerserker))
            generator.random.nextBoolean() -> shopItems.add(generator.nextItem(generator.berserkTemplate3, maxLvlBerserker))
        }
        //6,7,8
        shopItems.addAll(arrayOf(FlaskHpItem(), FlaskManaItem(), ScrollsItem()))
    }
}