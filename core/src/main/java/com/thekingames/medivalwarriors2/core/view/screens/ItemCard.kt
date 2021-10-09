package com.thekingames.medivalwarriors2.core.view.screens

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thekingames.items.ItemView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.shop.ClassSelector
import com.thekingames.medivalwarriors2.core.model.shop.Item
import com.thekingames.screenmanager.Fragment
import kotlinx.android.synthetic.main.c_item.view.*

class ItemCard(parent: ViewGroup, var item: Item) : Fragment(parent, R.layout.c_item) {
    lateinit var itemView: ItemView

    fun setOnClickListener(listener: View.OnClickListener) {
        view.setOnClickListener(listener)
    }

    fun remove() {
        parent.removeView(view)
    }

    override fun releaseDate() {
        super.releaseDate()
        itemView = ItemView(activity, item.idIcon,item.count)
        itemView.quality = item.quality
        view.item_view_container.addView(itemView.view)
        itemView.view.setOnClickListener({

        })
        item.stats.forEach {
            val text = View.inflate(activity, R.layout.stat_text, null) as TextView
            text.text = it.getDescription(activity)
            view.stats_container.addView(text)
        }
        view.item_name.setText(item.idName)

        (view.item_description.getChildAt(0) as TextView).setText(R.string.r_lvl)
        (view.item_description.getChildAt(1) as TextView).setText(R.string.item_quality)
        view.buy.text = item.cost.toString()

        (view.item_description.getChildAt(0) as TextView).append(item.lvl.toString())
        (view.item_description.getChildAt(1) as TextView).append(itemView.qualityName)
        view.icon.setImageResource(getClassIcon(item.classSelector))
    }

    fun setBuyListener(listener: View.OnClickListener) {
        view.buy.setOnClickListener(listener)
    }

    private fun getClassIcon(classSelector: ClassSelector): Int {
        return when (classSelector) {
            ClassSelector.Warrior -> R.drawable.icon_warrior
            ClassSelector.Hunter -> R.drawable.icon_hunter
            ClassSelector.Mage -> R.drawable.icon_mage
            ClassSelector.Rogue -> R.drawable.icon_rogue
            ClassSelector.Paladin -> R.drawable.icon_paladin
            ClassSelector.Berserk -> R.drawable.icon_berserk
            else -> {
                R.drawable.icon_group
            }
        }
    }

    fun setBuyEnable(value: Boolean) {
        view.buy.isEnabled = value
    }
}