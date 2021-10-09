package com.thekingames.medivalwarriors2.core.view.screens.dialog

import android.widget.Button
import android.widget.TextView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.screenmanager.Dialog
import kotlinx.android.synthetic.main.d_game_over.view.*

class GameDialog(private var a: MainActivity) : Dialog(a) {
    var idTextHeader: Int = R.string.player1

    override fun getLayoutId(): Int {
        return R.layout.d_game_over
    }

    override fun findView() {
        val textHeader: TextView = dialog.findViewById(R.id.text_header)
        textHeader.setText(idTextHeader)
        val mVictory: Int
        val collector = if (a.gameScreen!!.statCollectorPlayer1.isVictory) {
            mVictory = 2
            a.gameScreen!!.statCollectorPlayer1
        } else {
            mVictory = 1
            a.gameScreen!!.statCollectorPlayer2
        }
        val awardCoin: Int = mVictory * (collector.timeS * 0.5 + 20).toInt()
        val awardCrystal: Int = mVictory * (collector.timeS * 0.05 + 2).toInt()
        val awardExp: Int = mVictory * (collector.timeS * 1.5 + 25).toInt()
        if (a.player.access == a.chooseChapterScreen.chapterIndex &&
                a.gameScreen == a.gameCompany && mVictory == 2) {
            a.player.access++
        }
        dialog.award_coin.text = "$awardCoin"
        dialog.award_crystal.text = "$awardCrystal"
        dialog.award_exp.text = "$awardExp"
        val next = dialog.findViewById<Button>(R.id.next)
        next.setOnClickListener {
            parent.removeView(dialog)
            isShowingDialog = false
            val nextScreen = a.statScreen
            nextScreen.player1 = a.gameScreen!!.statCollectorPlayer1
            nextScreen.player2 = a.gameScreen!!.statCollectorPlayer2
            a.screenHistory.clear()
            a.screenHistory.add(a.menuScreen)
            a.setScreen(a.statScreen)
            a.player.coins += awardCoin
            a.player.crystals += awardCrystal
            a.player.getCurrentHero().exp += awardExp
        }
    }
}
