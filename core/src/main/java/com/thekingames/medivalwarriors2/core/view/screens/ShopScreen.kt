package com.thekingames.medivalwarriors2.core.view.screens

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import com.thekingames.items.ItemView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.model.shop.ClassSelector
import com.thekingames.medivalwarriors2.core.model.shop.FlaskHpItem
import com.thekingames.medivalwarriors2.core.model.shop.FlaskManaItem
import com.thekingames.medivalwarriors2.core.model.shop.SUPPORT_SLOT
import com.thekingames.medivalwarriors2.core.view.activity.INVENTORY
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.screenmanager.Fragment
import com.thekingames.screenmanager.Screen
import kotlinx.android.synthetic.main.a3_heroes.view.*
import kotlinx.android.synthetic.main.a4_inventory.view.*
import kotlinx.android.synthetic.main.a5_shop1.view.*
import kotlinx.android.synthetic.main.c_shop_hero.view.*

class Shop1Screen(var parent: ViewGroup) : Screen(parent, R.layout.a5_shop1) {
    private val a = activity as MainActivity;
    private lateinit var itemsCard: ArrayList<ItemCard>


    private fun showItems() {
        view.items_container.removeAllViews()
        itemsCard = arrayListOf()
        a.player.shopItems.forEach {
            val card = ItemCard(view.items_container, it)
            card.show()
            itemsCard.add(card)
            card.setBuyListener(View.OnClickListener {
                a.player.coins -= card.item.cost
                card.item.cost = (card.item.cost * 0.5).toInt()
                a.player.inventory.add(0, card.item)
                a.player.shopItems.remove(card.item)
                a.new(INVENTORY)
                card.remove()
                releaseItems()
            })
        }
        releaseItems()
    }

    private fun releaseItems() {
        itemsCard.forEach { it.setBuyEnable(it.item.cost <= a.player.coins) }
    }

    override fun releaseDate() {
        super.releaseDate()
        if (a.player.shopItems.isEmpty()) {
            a.player.generate()
        }
        if (view.items_container.childCount == 0) {
            showItems()
        }
        releaseItems()
        a.setHeader(R.string.b_shop)
    }
}

class ShopHeroesScreen(var parent: ViewGroup) : Screen(parent, R.layout.a3_heroes) {
    private val a = activity as MainActivity
    private lateinit var heroesCard: ArrayList<ShopHeroCard>

    override fun settingsView() {
        super.settingsView()
        view.hero_previous1.setOnClickListener {
            (view.content_container as ViewFlipper).inAnimation = animationFlipInForLeft
            (view.content_container as ViewFlipper).outAnimation = animationFlipOutForLeft
            (view.content_container as ViewFlipper).showPrevious()
        }
        view.hero_next1.setOnClickListener {
            (view.content_container as ViewFlipper).inAnimation = animationFlipInForRight
            (view.content_container as ViewFlipper).outAnimation = animationFlipOutForRight
            (view.content_container as ViewFlipper).showNext()
        }
    }

    private fun unActivateAll() {
        heroesCard.forEach { it.unActivate() }
    }

    private fun addShopHeroCard(hero: Unit) {
        heroesCard.add(ShopHeroCard(view.content_container, hero))
        heroesCard[heroesCard.size - 1].show()
        heroesCard[heroesCard.size - 1].setOnChooseListener(View.OnClickListener {
            a.player.currentHeroIndex = a.player.heroes.indexOf(hero)
            unActivateAll()
            heroesCard[a.player.currentHeroIndex].activate()
        })
    }

    private lateinit var animationFlipInForLeft: Animation
    private lateinit var animationFlipInForRight: Animation
    private lateinit var animationFlipOutForLeft: Animation
    private lateinit var animationFlipOutForRight: Animation

    override fun releaseDate() {
        super.releaseDate()
        view.content_container.removeAllViews()
        animationFlipInForLeft = AnimationUtils.loadAnimation(activity.applicationContext, R.anim.in_flip_for_left_button)
        animationFlipInForRight = AnimationUtils.loadAnimation(activity.applicationContext, R.anim.in_flip_for_right_button)
        animationFlipOutForLeft = AnimationUtils.loadAnimation(activity.applicationContext, R.anim.out_flip_for_left_button)
        animationFlipOutForRight = AnimationUtils.loadAnimation(activity.applicationContext, R.anim.out_flip_for_right_button)

        heroesCard = arrayListOf()
        a.player.heroes.forEach { addShopHeroCard(it) }
        heroesCard[a.player.currentHeroIndex].activate()
        a.setHeader(R.string.b_castle)
    }
}

class ShopHeroCard(var parent: ViewGroup, private var hero: Unit) : Fragment(parent, R.layout.c_shop_hero) {
    val a = activity as MainActivity
    private lateinit var card: HeroCardView

    override fun settingsView() {
        super.settingsView()
        view.exp.textVariant = 3
    }

    private fun showHeroCardView() {
        when (hero) {
            is Warrior -> {
                card = object : CardWarrior(view.hero_card_container) {
                    override fun getUnit(): Unit {
                        return hero
                    }
                }
                card.show()
            }
            is Hunter -> {
                card = object : CardHunter(view.hero_card_container) {
                    override fun getUnit(): Unit {
                        return hero
                    }
                }
                card.show()
            }
            is Mage -> {
                card = object : CardMage(view.hero_card_container) {
                    override fun getUnit(): Unit {
                        return hero
                    }
                }
                card.show()
            }
            is Rogue -> {
                card = object : CardRogue(view.hero_card_container) {
                    override fun getUnit(): Unit {
                        return hero
                    }
                }
                card.show()
            }
            is Paladin -> {
                card = object : CardPaladin(view.hero_card_container) {
                    override fun getUnit(): Unit {
                        return hero
                    }
                }
                card.show()
            }
            is Berserker -> {
                card = object : CardBerserk(view.hero_card_container) {
                    override fun getUnit(): Unit {
                        return hero
                    }
                }
                card.show()
            }
        }
    }

    fun setOnChooseListener(onClickListener: View.OnClickListener) {
        view.choose_hero.setOnClickListener(onClickListener)
    }

    fun activate() {
        view.setBackgroundResource(R.drawable.fon_trans)
    }

    fun unActivate() {
        view.background = null
    }

    interface Variant {
        fun next()
    }

    private fun releaseStats() {
        view.hero_name.setText(hero.nameId)
        view.stat_str.text = hero.force.toString()
        view.stat_agility.text = hero.agility.toString()
        view.stat_mind.text = hero.mind.toString()
        view.stat_e.text = hero.stamina.toString()
        view.exp.setMaxValue(hero.capExp)
        view.exp.setValue(hero.exp.toDouble())
        view.lvl.text = hero.lvl.toString()
        view.points.text = hero.points.toString()
    }

    private fun releaseStatController() {
        view.stat_controller.visibility = if (hero.points > 0) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    fun release() {
        releaseStats()
        releaseStatController()
        card.releaseCard()
        releaseItems()
        releaseSpells()
    }

    private fun releaseSpells() {

    }

    private fun releaseItems() {
        view.hero_slots.removeAllViews()
        view.spells.removeAllViews()
        val itemsView: ArrayList<ItemView> = arrayListOf()
        hero.weapons.forEach { itemsView.add(ItemView(a, it.idIcon, it.count)) }
        for (i in 0 until itemsView.size) {
            itemsView[i].quality = hero.weapons[i].quality
            view.hero_slots.addView(itemsView[i].view, 0)
            itemsView[i].view.setOnClickListener {
                a.chooseItemDialog.unit = hero
                a.chooseItemDialog.itemIndex = i
                a.chooseItemDialog.classSelector = hero.weapons[i].classSelector
                a.chooseItemDialog.type = hero.weapons[i].type
                a.chooseItemDialog.shopHeroCard = this
                a.chooseItemDialog.create(a.parent)
            }
        }
        hero.release()
        hero.spells.forEach {
            (it.view.parent as ViewGroup?)?.removeView(it.view)
            view.spells.addView(it.view)
        }
        hero.spells.forEach { it.view.unActivate() }
        view.add_item.setOnClickListener {
            a.chooseItemDialog.unit = hero
            a.chooseItemDialog.classSelector = ClassSelector.NONE
            a.chooseItemDialog.type = SUPPORT_SLOT
            a.chooseItemDialog.shopHeroCard = this
            a.chooseItemDialog.create(a.parent)
        }

        if (a.player.inventory.find { it is FlaskHpItem || it is FlaskManaItem } == null) {
            view.add_item.visibility = View.GONE
        }
    }

    override fun releaseDate() {
        super.releaseDate()
        showHeroCardView()

        releaseStats()
        releaseStatController()
        releaseItems()
        val variants: ArrayList<Variant> = arrayListOf()
        variants.add(object : Variant {
            override fun next() {
                hero.force++
            }
        })
        variants.add(object : Variant {
            override fun next() {
                hero.agility++
            }
        })
        variants.add(object : Variant {
            override fun next() {
                hero.mind++
            }
        })
        variants.add(object : Variant {
            override fun next() {
                hero.stamina++
            }
        })
        for (i in 0 until view.stat_controller.childCount) {
            view.stat_controller.getChildAt(i).setOnClickListener {
                if (hero.points > 0) {
                    hero.points--
                    releaseStatController()
                    variants[i].next()
                    releaseStats()
                    card.releaseCard()
                }
            }
        }
    }
}

class InventoryScreen(var parent: ViewGroup) : Screen(parent, R.layout.a4_inventory) {
    private val a = activity as MainActivity
    private lateinit var itemsCard: ArrayList<ItemCard>

    override fun releaseDate() {
        super.releaseDate()
        view.inventory.removeAllViews()
        itemsCard = arrayListOf()
        a.player.inventory.forEach {
            val card = ItemCard(view.inventory, it)
            card.show()
            itemsCard.add(card)
            card.setBuyListener(View.OnClickListener {
                a.player.coins += card.item.cost
                a.player.inventory.remove(card.item)
                card.remove()
            })
        }
        a.setHeader(R.string.b_inventory)
    }
}