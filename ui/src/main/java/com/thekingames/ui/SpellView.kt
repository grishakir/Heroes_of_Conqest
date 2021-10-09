package com.thekingames.ui

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView

const val UNSUPPORTED = 0

abstract class SpellView(context: Context) : RelativeLayout(context) {

    fun getSkillIcon(): SkillIcon {
        return getChildAt(0) as SkillIcon
    }

    fun activate() {
        getSkillIcon().activate()
    }

    fun unActivate() {
        getSkillIcon().unActivate()
    }

    fun touch(callDown: Double): Boolean {
        return getSkillIcon().touch(callDown)
    }

    fun globalCallDown(gCallDown: Double) {
        getSkillIcon().globalCallDown(gCallDown)
    }

    fun setRest(value: Double) {
        getSkillIcon().rest = value
    }

    fun setGRest(value: Double) {
        getSkillIcon().gRest = value
    }

    var count = UNSUPPORTED
        set(value) {
            field = value
            if (value > UNSUPPORTED) {
                (counterView.findViewById<TextView>(R.id.text_count)).text = value.toString()
            }
        }

    private val counterView: RelativeLayout by lazy {
        return@lazy View.inflate(context, R.layout.text_counter, this) as RelativeLayout
    }

}

//region warrior
class SpellCuttingBlow(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_cutting_blow, this))
}
}

class SpellReflection(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_reflection, this))
}
}

class SpellDeepWound(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_deep_wound, this))
}
}

class SpellThunderstorm(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_thunderstorm, this))
}
}

class SpellMadness(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_madness, this))
}
}

//endregion warrior

//region hunter

class SpellLightningBolt(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_lightning_bolt, this))
}
}

class SpellTrap(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_trap, this))
}
}

class SpellPiercingShot(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_piercing_shot, this))
}
}

class SpellVengeful(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_vengeful, this))
}
}

class SpellAuraOfNature(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_aura_of_nature, this))
}
}

//endregion hunter

//region mage

class SpellWitchcraft(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_witchcraft, this))
}
}

class SpellSilence(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_silence, this))
}
}

class SpellFrostBolt(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_frost_bolt, this))
}
}

class SpellDistributionShield(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_distribution_shield, this))
}
}

class SpellBlast(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_blast, this))
}
}
//endregion mage

//region rogue
class SpellDaggerThrow(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_dagger_throw, this))
}
}

class SpellSuddenBlow(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_sudden_blow, this))
}
}

class SpellSmoke(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_smoke, this))
}
}

class SpellPoisoning(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_poisoning, this))
}
}

class SpellMastery(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_mastery, this))
}
}
//endregion rogue

//region paladin
class SpellCrushingBlow(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_crushing_blow, this))
}
}

class SpellFlash(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_flash, this))
}
}

class SpellHolyAura(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_holy_aura, this))
}
}

class SpellShieldBash(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_shield_bash, this))
}
}

class SpellHealing(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_healing, this))
}
}
//endregion


//region berserker
class SpellBerserk(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_berserk, this))
}
}

class SpellDisarming(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_disarming, this))
}
}

class SpellCripplingStrike(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_crippling_strike, this))
}
}

class SpellLunge(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_lunge, this))
}
}

class SpellManaBurn(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_mana_burn, this))
}
}

//endregion

//region t1
class SpellT1MagicShield(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.t1_magic_shield, this))
}
}

class SpellT1Freedom(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.t1_freedom, this))
}
}

class SpellT1DispelMagic(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.t1_dispel_magic, this))
}
}
//endregion

//region flasks
class SpellHpFlask(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_flask_hp, this))
}
}

class SpellManaFlask(context: Context) : SpellView(context) {init {
    (inflate(context, R.layout.skill_flask_mana, this))
}
}
//endregion



