package com.thekingames.medivalwarriors2.core.model.shop

import com.thekingames.items.R
import java.util.*

class ItemGenerator(private val maxLvl: Int, private val maxQuality: Int) {
    val random = Random(System.currentTimeMillis())

    val warriorTemplate1: Item = WarriorItem(1, 0, 200, R.drawable.sword, R.string.sword_w_name1, TWO_HANDED)
    val warriorTemplate2: Item = WarriorItem(1, 5, 400, R.drawable.upg_sword, R.string.sword_w_name2, TWO_HANDED)

    val hunterTemplate1: Item = HunterItem(1, 0, 200, R.drawable.bow, R.string.bow_name1, TWO_HANDED)
    val hunterTemplate2: Item = HunterItem(1, 5, 400, R.drawable.upg_bow, R.string.bow_name2, TWO_HANDED)

    val mageTemplate1: Item = MageItem(1, 0, 100, R.drawable.wand, R.string.wand_name1, LEFT_HAND)
    val mageTemplate2: Item = MageItem(1, 5, 300, R.drawable.upg_wand, R.string.wand_name2, LEFT_HAND)

    val mageTemplate3: Item = MageItem(1, 0, 100, R.drawable.tome, R.string.tome_name, RIGHT_HAND)

    val rogueTemplate1: Item = RogueItem(1, 0, 100, R.drawable.dagger, R.string.dagger_name1, ONE_HANDED)
    val rogueTemplate2: Item = RogueItem(1, 5, 200, R.drawable.upg_dagger, R.string.dagger_name2, ONE_HANDED)

    val paladinTemplate1: Item = PaladinItem(1, 0, 100, R.drawable.sword, R.string.sword_p_name1, LEFT_HAND)
    val paladinTemplate2: Item = PaladinItem(1, 5, 200, R.drawable.sword, R.string.sword_p_name2, LEFT_HAND)

    val paladinTemplate3: Item = PaladinItem(1, 0, 100, R.drawable.shield, R.string.shield_name1, RIGHT_HAND)
    val paladinTemplate4: Item = PaladinItem(1, 5, 200, R.drawable.shield, R.string.shield_name2, RIGHT_HAND)

    val berserkTemplate1: Item = BerserkItem(1, 0, 100, R.drawable.axe, R.string.axe_name1, ONE_HANDED)
    val berserkTemplate2: Item = BerserkItem(1, 0, 200, R.drawable.axe2, R.string.axe_name2, ONE_HANDED)
    val berserkTemplate3: Item = BerserkItem(1, 5, 400, R.drawable.upg_axe1, R.string.axe_name3, ONE_HANDED)

    val templates = arrayOf(
            warriorTemplate1,
            warriorTemplate2,
            hunterTemplate1,
            hunterTemplate2,
            mageTemplate1,
            mageTemplate2,
            mageTemplate3,
            rogueTemplate1,
            rogueTemplate2,
            paladinTemplate1,
            paladinTemplate2,
            paladinTemplate3,
            paladinTemplate4,
            berserkTemplate1,
            berserkTemplate1,
            berserkTemplate2,
            berserkTemplate3
    )

    fun nextItem(): Item {
        return nextItem(templates[random.nextInt(templates.size)])
    }

    fun nextItem(template: Item): Item {
        return nextItem(template, maxLvl)
    }

    fun nextItem(template: Item, maxLvl: Int): Item {
        val randomItem = template.copy()
        val maxQuality = maxQuality - randomItem.quality
        randomItem.quality += random.nextInt(maxQuality)
        randomItem.lvl = random.nextInt(maxLvl) + 1
        randomItem.cost = (randomItem.lvl) * randomItem.cost * (randomItem.quality + 1)
        val rest: Int = 1 + randomItem.cost / 500
        randomItem.stats.addAll(nextStats(randomItem, rest))
        val statDmg = (rest / 10)
        if (statDmg != 0) {
            randomItem.stats.add(StatDmg(statDmg))
        }
        return randomItem
    }

    private fun nextStats(item: Item, rest: Int): ArrayList<Stat> {
        var mStatA = 0
        var mStatS = 0
        var mStatM = 0
        var mStatE = 0
        val stats: ArrayList<Stat> = arrayListOf()
        when (item.classSelector) {
            ClassSelector.Warrior -> {
                mStatA = random.nextInt(rest)
                mStatS = random.nextInt(rest - mStatA)
                mStatE = rest - mStatA - mStatS
            }
            ClassSelector.Hunter -> {
                mStatM = random.nextInt(rest)
                mStatS = random.nextInt(rest - mStatM)
                mStatE = rest - mStatM - mStatS
            }
            ClassSelector.Mage -> {
                mStatM = random.nextInt(rest)
                mStatE = rest - mStatM
            }
            ClassSelector.Rogue -> {
                mStatA = random.nextInt(rest)
                mStatE = rest - mStatA
            }
            ClassSelector.Paladin -> {
                mStatM = random.nextInt(rest)
                mStatS = random.nextInt(rest - mStatM)
                mStatE = rest - mStatM - mStatS
            }
            ClassSelector.Berserk -> {
                mStatA = random.nextInt(rest)
                mStatS = random.nextInt(rest - mStatA)
                mStatE = rest - mStatA - mStatS
            }
            ClassSelector.NONE -> {

            }
        }
        if (mStatA != 0) {
            stats.add(StatA(mStatA))
        }
        if (mStatS != 0) {
            stats.add(StatS(mStatS))
        }
        if (mStatM != 0) {
            stats.add(StatM(mStatM))
        }
        if (mStatE != 0) {
            stats.add(StatE(mStatE))
        }
        return stats
    }
}