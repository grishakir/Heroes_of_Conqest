package com.thekingames.medivalwarriors2.core.view.screens

import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.CostType
import com.thekingames.medivalwarriors2.core.model.Spell
import com.thekingames.screenmanager.Fragment


class SpellCardView(private var spell: Spell, parent: ViewGroup) : Fragment(parent, R.layout.c_spell) {
    override fun settingsView() {
        super.settingsView()

    }

    override fun releaseDate() {
        super.releaseDate()
        val icon = view.findViewById<ImageView>(R.id.icon)
        val name = view.findViewById<TextView>(R.id.name)
        val callDown = view.findViewById<TextView>(R.id.call_down)
        val cost = view.findViewById<TextView>(R.id.cost)
        val desc = view.findViewById<TextView>(R.id.desc)
        icon.setImageDrawable(spell.view.getSkillIcon().background)
        name.text = spell.name
        callDown.append(spell.callDown.toString() + activity.getString(R.string.second))
        desc.text = spell.description

        val resTypeText = SpannableString(activity.resources.getString(spell.resId))
        val color: Int = when (spell.costType) {
            CostType.RAGE -> {
                R.color.rage1
            }
            CostType.MANA -> {
                R.color.mana1
            }
            CostType.ENERGY -> {
                R.color.energy1
            }
        }
        resTypeText.setSpan(ForegroundColorSpan(ContextCompat.getColor(activity,color)), 0, resTypeText.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        val costValue = SpannableString(spell.cost.toString())
        costValue.setSpan(ForegroundColorSpan(ContextCompat.getColor(activity,color)), 0, costValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        val concatenated = TextUtils.concat(cost.text, costValue, " ", resTypeText) as Spanned
        val costText = SpannableStringBuilder(concatenated)
        cost.setText(costText, TextView.BufferType.SPANNABLE)

    }
}