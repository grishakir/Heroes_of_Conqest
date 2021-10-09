package com.thekingames.medivalwarriors2.core.view.screens.dialog

import android.widget.TableLayout
import android.widget.TableRow
import com.thekingames.items.ItemView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.SpellHpFlask
import com.thekingames.medivalwarriors2.core.model.SpellManaFlask
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.model.shop.*
import com.thekingames.medivalwarriors2.core.view.activity.INVENTORY
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.medivalwarriors2.core.view.screens.ShopHeroCard
import com.thekingames.screenmanager.Dialog
import kotlinx.android.synthetic.main.d_choose_item.view.*

class ChooseItemDialog(private var a: MainActivity) : Dialog(a) {
    lateinit var unit: Unit
    lateinit var shopHeroCard: ShopHeroCard
    var itemIndex: Int = 0
    var type: Int = NONE
    var classSelector: ClassSelector = ClassSelector.Warrior
    private var sizeX: Int = 5

    override fun getLayoutId(): Int {
        return R.layout.d_choose_item
    }

    fun hide() {
        isShowingDialog
    }

    override fun findView() {
        dialog.close_dialog.setOnClickListener {
            parent.removeView(dialog)
            isShowingDialog = false
        }
        val table: TableLayout = dialog.item_table
        table.addView(TableRow(activity))
        fun addItem(item: Item) {
            var tableRow = table.getChildAt(table.childCount - 1) as TableRow
            fun addItemView() {
                val itemView = ItemView(a, item.idIcon,item.count)
                itemView.quality = item.quality
                tableRow.addView(itemView.view)
                itemView.view.setOnClickListener {
                    if (unit.weapons[itemIndex].classSelector == item.classSelector &&
                            unit.lvl >= item.lvl &&
                            unit.weapons[itemIndex].type == item.type) {
                        a.player.inventory.add(0, unit.weapons[itemIndex])
                        a.new(INVENTORY)
                        unit.resetWeapon(itemIndex, item)
                        a.player.inventory.remove(item)
                        parent.removeView(dialog)
                        isShowingDialog = false
                        shopHeroCard.release()
                    } else if (item.type == SUPPORT_SLOT) {
                        if (unit.flaskHp == null && item is FlaskHpItem) {
                            a.player.inventory.remove(item)
                            unit.flaskHp = item
                            unit.spells.first { it is SpellHpFlask }.load(a)
                            parent.removeView(dialog)
                            isShowingDialog = false
                            shopHeroCard.release()
                        } else if (unit.flaskMana == null && item is FlaskManaItem) {
                            a.player.inventory.remove(item)
                            unit.flaskMana = item
                            unit.spells.first { it is SpellManaFlask }.load(a)
                            parent.removeView(dialog)
                            isShowingDialog = false
                            shopHeroCard.release()
                        } else if (item is ScrollsItem) {
                            item.apply(unit)
                            a.player.inventory.remove(item)
                            parent.removeView(dialog)
                            isShowingDialog = false
                            shopHeroCard.release()
                        }
                    }
                }
            }
            if (tableRow.childCount < sizeX) {
                addItemView()
            } else {
                table.addView(TableRow(activity))
                tableRow = table.getChildAt(table.childCount - 1) as TableRow
                addItemView()
            }
        }
        a.player.inventory.forEach {
            if (it.type == type &&
                    it.classSelector == classSelector) {
                addItem(it)
            }
        }

    }
}