package com.thekingames.medivalwarriors2.core.view.screens.dialog

import android.widget.Button
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.medivalwarriors2.core.view.screens.GameScreen
import com.thekingames.screenmanager.Dialog

class MenuDialog(private var a: MainActivity) : Dialog(a) {
    lateinit var gameScreen: GameScreen

    override fun getLayoutId(): Int {
        return R.layout.d_menu
    }

    override fun findView() {
        gameScreen.onPause()
        val next = dialog.findViewById<Button>(R.id.next)
        next.setOnClickListener({
            isShowingDialog = false
            parent.removeView(dialog)
            gameScreen.onResume()
        })
        val restart = dialog.findViewById<Button>(R.id.restart)
        val backward = dialog.findViewById<Button>(R.id.backward)
        val gotoMenu = dialog.findViewById<Button>(R.id.goto_menu)
        restart.setOnClickListener({
            isShowingDialog = false
            gameScreen.gameEnd()
            gameScreen.show()
        })
        backward.setOnClickListener({
            isShowingDialog = false
            gameScreen.gameEnd()
            a.screenHistory.removeAt(a.screenHistory.size - 1)
            a.setScreen(a.screenHistory[a.screenHistory.size - 1])
        })
        gotoMenu.setOnClickListener({
            isShowingDialog = false
            gameScreen.gameEnd()
            a.screenHistory.clear()
            a.setScreen(a.menuScreen)
            a.showBar()
        })
    }
}