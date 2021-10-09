package com.thekingames.ui

import android.content.Context
import android.view.View
import android.view.View.inflate

abstract class TextBar(var context: Context, idBackground: Int) {
    var root: View = inflate(context, R.layout.bar, null)
    var view: ProgressBar = root.findViewById(R.id.bar)

    init {
        view.setBackgroundResource(idBackground)
    }
}

open class TextBarHp(context: Context, idBackground: Int) : TextBar(context, idBackground) {init {
    view.textVariant = 2
}
}

class TextBarHp1(context: Context) : TextBarHp(context, R.drawable.bar_hp1)

class TextBarHp2(context: Context) : TextBarHp(context, R.drawable.bar_hp2)

open class TextBarRage(context: Context, idBackground: Int) : TextBar(context, idBackground)

class TextBarRage1(context: Context) : TextBarRage(context, R.drawable.bar_rage1)

class TextBarRage2(context: Context) : TextBarRage(context, R.drawable.bar_rage2)

open class TextBarEnergy(context: Context, idBackground: Int) : TextBar(context, idBackground)

class TextBarEnergy1(context: Context) : TextBarEnergy(context, R.drawable.bar_energy1)

class TextBarEnergy2(context: Context) : TextBarEnergy(context, R.drawable.bar_energy2)

open class TextBarMana(context: Context, idBackground: Int) : TextBar(context, idBackground)

class TextBarMana1(context: Context) : TextBarMana(context, R.drawable.bar_magic1)

class TextBarMana2(context: Context) : TextBarMana(context, R.drawable.bar_magic2)


