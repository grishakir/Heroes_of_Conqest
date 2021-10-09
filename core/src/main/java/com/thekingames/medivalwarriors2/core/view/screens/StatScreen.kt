package com.thekingames.medivalwarriors2.core.view.screens

import android.support.v4.content.ContextCompat
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.Spell
import com.thekingames.medivalwarriors2.core.model.StatCollector
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.screenmanager.Fragment
import com.thekingames.screenmanager.Screen
import kotlinx.android.synthetic.main.game_statistic.view.*
import kotlinx.android.synthetic.main.s_actions.view.*
import kotlinx.android.synthetic.main.s_triple.view.*
import java.util.*

const val PLAYER1 = 1
const val PLAYER2 = 2

class StatScreen(var parent: ViewGroup) : Screen(parent, R.layout.game_statistic) {
    private val a = activity as MainActivity
    lateinit var player1: StatCollector
    lateinit var player2: StatCollector
    lateinit var actions: ArrayList<ActionDate>

    override fun settingsView() {
        super.settingsView()
        view.next.setOnClickListener {
            a.screenHistory.clear()
            a.setScreen(a.menuScreen)
            a.showBar()
        }
    }

    override fun releaseDate() {
        super.releaseDate()
        view.spells_history.removeAllViews()
        view.show_history.setOnClickListener {
            if (view.spells_history.childCount == 0) {
                actions.forEach { Actions(view.spells_history, it).show() }
            } else {
                if (view.spells_history.visibility == View.VISIBLE) {
                    view.spells_history.visibility = View.GONE
                } else {
                    view.spells_history.visibility = View.VISIBLE
                }
            }
        }
        fun getPlayerHeaderBackground(boolean: Boolean): Int {
            return if (boolean) {
                R.drawable.ribbon
            } else {
                R.drawable.ribbon_lose
            }
        }
        view.game_end.text = player1.getTime()

        view.player1.setBackgroundResource(getPlayerHeaderBackground(player1.isVictory))
        view.player2.setBackgroundResource(getPlayerHeaderBackground(player2.isVictory))

        val player1DataArray = arrayOf(player1.damage.toInt(), player1.maxDamage.toInt(), player1.aDamage().toInt(), player1.heal.toInt(), player1.maxHeal.toInt())
        val player2DataArray = arrayOf(player2.damage.toInt(), player2.maxDamage.toInt(), player2.aDamage().toInt(), player2.heal.toInt(), player2.maxHeal.toInt())
        val idTexts = arrayOf(R.string.damage, R.string.max_damage, R.string.a_damage, R.string.heal, R.string.max_heal)
        for (i in 0 until view.left_side.childCount) {
            (view.left_side.getChildAt(i) as TextView).setText(idTexts[i])
            (view.left_side.getChildAt(i) as TextView).append(player1DataArray[i].toString())
        }
        for (i in 0 until view.right_side.childCount) {
            (view.right_side.getChildAt(i) as TextView).setText(idTexts[i])
            (view.right_side.getChildAt(i) as TextView).append(player2DataArray[i].toString())
        }
        //region warning HARD CODE
        actions = arrayListOf()
        val uniqueTimes: ArrayList<Int> = arrayListOf()
        val triples: ArrayList<TripleDate> = arrayListOf()

        for (i in 0 until player1.spellHistory.size) {
            triples.add(TripleDate(player1.spellHistory[i], player1.damageHistory[i], player1.timeHistory[i], PLAYER1))
        }

        for (i in 0 until player2.spellHistory.size) {
            triples.add(TripleDate(player2.spellHistory[i], player2.damageHistory[i], player2.timeHistory[i], PLAYER2))
        }

        triples.sortBy { it.time }

        for (i in 0 until triples.size) {
            if (!uniqueTimes.contains(triples[i].time.toInt())) {
                uniqueTimes.add(triples[i].time.toInt())
            }
        }
        uniqueTimes.forEach {
            val actionDate = ActionDate(arrayListOf(), arrayListOf())
            val currentMs = it
            val list = triples.filter {
                it.time.toInt() == currentMs
            }
            list.forEach {
                if (it.player == PLAYER1) {
                    actionDate.actionsPlayer1.add(it)
                } else if (it.player == PLAYER2) {
                    actionDate.actionsPlayer2.add(it)
                }
            }
            actions.add(actionDate)
        }
        //endregion
    }
}

data class ActionDate(val actionsPlayer1: ArrayList<TripleDate>, val actionsPlayer2: ArrayList<TripleDate>)

class Actions(parent: ViewGroup, var action: ActionDate) : Fragment(parent, R.layout.s_actions) {
    override fun releaseDate() {
        super.releaseDate()
        action.actionsPlayer1.forEach { Triple(view.actions_player1, it).show() }
        action.actionsPlayer2.forEach { Triple(view.actions_player2, it).show() }
    }
}

data class TripleDate(val spell: Spell, val damage: String, val time: Double, val player: Int)

class Triple(parent: ViewGroup, var tripleDate: TripleDate) : Fragment(parent, R.layout.s_triple) {
    override fun releaseDate() {
        super.releaseDate()
        view.icon.setImageResource(tripleDate.spell.view.getSkillIcon().iconId)
        if (tripleDate.damage.contains("+")) {
            view.damage.setTextColor(ContextCompat.getColor(activity, R.color.heal))
        }
        view.damage.text = tripleDate.damage
        view.time.text = getTime()
    }

    fun getTime(): String {
        val date = Date()
        date.time = (tripleDate.time * 1000).toLong()
        return DateFormat.format("mm:ss", date).toString()
    }
}