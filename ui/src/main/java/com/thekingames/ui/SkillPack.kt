package com.thekingames.ui

import android.content.Context
import android.util.AttributeSet


//region warrior
class CuttingBlow @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_cutting_blow, R.drawable.fon_rage)

class DeepWound @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_deep_wound, R.drawable.fon_rage)

class Reflection @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_reflection, R.drawable.fon_rage)

class Thunderstorm @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_thunderstorm, R.drawable.fon_rage)

class Madness @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_madness, R.drawable.fon_rage)
//endregion warrior

//region hunter
class LightningBolt @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_lightning_bolt, R.drawable.fon_mana)


class Trap @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_trap, R.drawable.fon_energy)

class PiercingShot @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_piercing_shot, R.drawable.fon_energy)

class Vengeful @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_vengeful, R.drawable.fon_energy)

class AuraOfNature @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_aura_of_nature, R.drawable.fon_mana)

//endregion hunter

//region mage
class Witchcraft @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_witchcraft, R.drawable.fon_mana)

class Silence @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_silence, R.drawable.fon_mana)

class FrostBolt @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_frost_bolt, R.drawable.fon_mana)

class DistributionShield @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_distribution_shield, R.drawable.fon_mana)

class Blast @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_blast, R.drawable.fon_mana)
//endregion mage

//region rogue
class DaggerThrow @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_dagger_throw, R.drawable.fon_energy)

class SuddenBlow @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_sudden_blow, R.drawable.fon_energy)

class Smoke @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_smoke, R.drawable.fon_energy)

class Poisoning @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_poisoning, R.drawable.fon_energy)

class Mastery @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_mastery, R.drawable.fon_energy)
//endregion rogue

//region paladin
class CrushingBlow @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_crushing_blow, R.drawable.fon_rage)

class Flash @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_flash, R.drawable.fon_mana)

class HolyAura @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_holy_aura, R.drawable.fon_mana)

class ShieldBash @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_shield_bash, R.drawable.fon_rage)

class Healing @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_healing, R.drawable.fon_mana)
//endregion

//region berserker
class Berserk @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_berserk, R.drawable.fon_rage)
class Disarming @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_disarming, R.drawable.fon_energy)
class CripplingStrike @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_crippling_strike, R.drawable.fon_energy)
class Lunge @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_lunge, R.drawable.fon_rage)
class ManaBurn @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_mana_burn, R.drawable.fon_energy)
//endregion

//region t1
class T1MagicShield @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.t1_magic_shield, R.drawable.fon_mana)
class T1Freedom @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.t1_freedom, R.drawable.fon_mana)
class T1DispelMagic @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.t1_dispel_magic, R.drawable.fon_mana)
//endregion

//region flask
class ManaFlask @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_flask, R.drawable.fon_mana)

class HpFlask @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        SkillIcon(context, attrs, defStyleAttr, R.drawable.skill_flask, R.drawable.fon_rage)
//endregion