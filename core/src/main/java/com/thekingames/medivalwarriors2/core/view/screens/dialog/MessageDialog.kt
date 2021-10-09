package com.thekingames.medivalwarriors2.core.view.screens.dialog

import android.view.View
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.screenmanager.Dialog
import kotlinx.android.synthetic.main.d_message.view.*


class MessageDialog(private var a: MainActivity) : Dialog(a) {

    var coins: Int = 10
        set(value) {
            field = value
            dialog.award_coin.text = value.toString()
        }
    var crystalls: Int = 1
        set(value) {
            field = value
            dialog.award_crystal.text = value.toString()
        }
    var exp: Int = 100
        set(value) {
            field = value
            dialog.award_exp.text = value.toString()
        }
    var result = false
        set(value) {
            field = value
            if (value) {
                dialog.text_header.text = a.getString(R.string.luck)
                dialog.text_header.setBackgroundResource(R.drawable.ribbon)
                dialog.awards.visibility = View.VISIBLE
            } else {
                dialog.text_header.text = a.getString(R.string.failure)
                dialog.text_header.setBackgroundResource(R.drawable.ribbon_lose)
                dialog.awards.visibility = View.GONE
            }
        }

    var textDescription = 0
        set(value) {
            field = value
            dialog.text_desc.setText(textDescription)
        }

    override fun getLayoutId(): Int {
        return R.layout.d_message
    }

    override fun findView() {
        dialog.next.setOnClickListener { parent.removeView(dialog) }
    }
}