package com.thekingames.medivalwarriors2.core.view.screens

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.medivalwarriors2.core.view.activity.Settings
import com.thekingames.screenmanager.Screen

class MenuScreen(var parent: ViewGroup) : Screen(parent, R.layout.a1_menu) {
    lateinit var contentMenu: LinearLayout
    private val a = activity as MainActivity


    override fun settingsView() {
        super.settingsView()
        contentMenu = view.findViewById(R.id.content_menu)
        contentMenu.getChildAt(3).visibility = if (Settings.TEST_ENABLE) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun releaseDate() {
        super.releaseDate()
        a.setHeader(R.string.b_battle)
    }
}