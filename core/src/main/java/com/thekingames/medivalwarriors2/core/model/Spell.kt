package com.thekingames.medivalwarriors2.core.model

import android.content.Context
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.interfaces.ActionEffect
import com.thekingames.medivalwarriors2.core.model.interfaces.ActionSpell
import com.thekingames.medivalwarriors2.core.model.interfaces.OnMoveEffectsListener
import com.thekingames.medivalwarriors2.core.model.interfaces.OnTickListener
import com.thekingames.ui.*
import com.thekingames.ui.SpellAuraOfNature
import com.thekingames.ui.SpellBerserk
import com.thekingames.ui.SpellBlast
import com.thekingames.ui.SpellCripplingStrike
import com.thekingames.ui.SpellCrushingBlow
import com.thekingames.ui.SpellCuttingBlow
import com.thekingames.ui.SpellDaggerThrow
import com.thekingames.ui.SpellDeepWound
import com.thekingames.ui.SpellDisarming
import com.thekingames.ui.SpellDistributionShield
import com.thekingames.ui.SpellFlash
import com.thekingames.ui.SpellFrostBolt
import com.thekingames.ui.SpellHealing
import com.thekingames.ui.SpellHolyAura
import com.thekingames.ui.SpellHpFlask
import com.thekingames.ui.SpellLightningBolt
import com.thekingames.ui.SpellLunge
import com.thekingames.ui.SpellMadness
import com.thekingames.ui.SpellManaBurn
import com.thekingames.ui.SpellManaFlask
import com.thekingames.ui.SpellMastery
import com.thekingames.ui.SpellPiercingShot
import com.thekingames.ui.SpellPoisoning
import com.thekingames.ui.SpellReflection
import com.thekingames.ui.SpellShieldBash
import com.thekingames.ui.SpellSilence
import com.thekingames.ui.SpellSmoke
import com.thekingames.ui.SpellSuddenBlow
import com.thekingames.ui.SpellT1DispelMagic
import com.thekingames.ui.SpellT1Freedom
import com.thekingames.ui.SpellT1MagicShield
import com.thekingames.ui.SpellThunderstorm
import com.thekingames.ui.SpellTrap
import com.thekingames.ui.SpellVengeful
import com.thekingames.ui.SpellWitchcraft
import java.io.Serializable
import java.util.*

enum class CostType : Serializable {
    RAGE, ENERGY, MANA
}

abstract class Spell(var costType: CostType) : OnTickListener, Serializable {
    abstract val cost: Int
    abstract var callDown: Int
    abstract var actionEvent: ActionSpell

    var isReady: Boolean = true
        get() {
            return rest == 0.0
        }

    var rest = 0.0
        set(value) {
            field = if (value > 0.0) {
                value
            } else {
                0.0
            }
        }

    var gRest = 0.0
        set(value) {
            field = if (value > 0.0) {
                value
            } else {
                0.0
            }
        }

    private val p: String = "%"

    abstract val name: String
    abstract val description: String

    @Transient
    lateinit var view: SpellView

    var count: Int = 0

    @Transient
    protected lateinit var context: Context

    val resId by lazy {
        when (costType) {
            CostType.RAGE -> R.string.part_rage
            CostType.ENERGY -> R.string.part_energy
            CostType.MANA -> R.string.part_mana
        }
    }

    var commentId = 0
    var soundId = 0
    open val commentResId = 0
    open val soundResId = 0

    var rotationCondition: Boolean = true

    open fun canUse(unit: Unit): Boolean {
        return unit.isControl
    }

    protected fun toPercent(value: Double): String {
        return "${(value * 100).toInt()}$p"
    }

    protected fun existTarget(unit: Unit): Boolean {
        return unit.target != null
    }

    fun use(target: Unit?, initiator: Unit) {
        actionEvent.action(target, initiator)
        initiator.onSpellUseListeners.forEach { it.onSpellUse(this) }
    }

    fun touch(callDown: Double): Boolean {
        if (rest == 0.0 && gRest == 0.0) {
            rest = callDown
            view.touch(callDown)
            return true
        }
        return false
    }

    fun globalCallDown(gCallDown: Double) {
        if (gRest == 0.0) {
            gRest = gCallDown
            view.globalCallDown(gCallDown)
        }
    }

    override fun onTick() {
        if (rest > 0) {
            rest -= 0.1
            view.setRest(rest)
        } else {
            rest = 0.0
        }
        gRest -= 0.1
        view.setGRest(gRest)
    }

    abstract fun loadView(context: Context)

    fun load(context: Context) {
        loadView(context)
        this.context = context
    }
}

interface SpellDamage {
    var mDamage: Double
}

interface SpellBurst {
    var burstDuration: Double
}

interface SpellCC {
    var controlDuration: Double
}

interface SpellDefence {
    var defUpDuration: Double
    var defUpValue: Double
}

interface SpellHeal {
    var mHeal: Double
}

//region warrior
class SpellCuttingBlow : Spell(CostType.RAGE), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellCuttingBlow(context)
    }

    override val description: String
        get() {
            return context.getString(R.string.spell_dam, toPercent(mDamage))
        }
    override val name: String
        get() {
            return context.getString(R.string.spell_name_cutting_blow)
        }

    override val soundResId by lazy { return@lazy R.raw.w_1 }

    override val cost = 10
    override var callDown = 2
    override var mDamage = 2.0

    override var actionEvent = ActionSpell { target, initiator ->
        initiator.rage -= cost
        initiator.damageEvent(mDamage * initiator.damage, target, this)
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.rage >= cost && unit.isArming && existTarget(unit)
    }

}

class SpellReflection : Spell(CostType.RAGE) {
    override fun loadView(context: Context) {
        view = SpellReflection(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_reflection)
        }

    override val description: String
        get() {
            return context.getString(R.string.spell_reflection, duration.toString())
        }
    override val soundResId by lazy { return@lazy R.raw.w_2 }
    override val commentResId by lazy { return@lazy R.raw.w_spell2 }

    override val cost = 40
    override var callDown = 50
    private val duration = 4.0
    override var actionEvent = ActionSpell { _, initiator ->
        initiator.rage -= cost
        initiator.effects.add(ReflectionEffect(context, duration, false).start(initiator))
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.rage >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellDeepWound : Spell(CostType.RAGE), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellDeepWound(context)
        effect = WoundEffect(context, dotDuration, mDotDamage, maxCount)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_deep_wound)
        }

    override val description: String
        get() {
            val textDamage = context.getString(R.string.spell_dam, toPercent(mDamage))
            val textDotDamage = context.getString(R.string.spell_dot, dotDuration.toInt(), toPercent(mDotDamage), maxCount)
            return """$textDamage
                |$textDotDamage""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.w_3 }
    override val cost = 20
    override var callDown = 5
    override var mDamage = 2.4
    private val dotDuration = 10.0
    private val mDotDamage = 0.05
    private val maxCount = 5
    @Transient
    lateinit var effect: Effect
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.rage -= cost
        initiator.damageEvent(mDamage * initiator.damage, target, this)

        var effectFound = false
        target.effects.forEach {
            if (it.javaClass == effect.javaClass) {
                it.currentCount++
                effectFound = true
                return@forEach
            }
        }
        if (!effectFound) {
            target.effects.add(effect.copy().start(target, initiator))
        }
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.rage >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellThunderstorm : Spell(CostType.RAGE), SpellCC, SpellDamage {
    override fun loadView(context: Context) {
        view = SpellThunderstorm(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_thunderstorm)
        }

    override val description: String
        get() {
            val textDamage = context.getString(R.string.spell_dam, toPercent(mDamage))
            val textStun = context.getString(R.string.spell_stun, controlDuration.toString())
            val textDefDown = context.getString(R.string.spell_def_d, defDownDuration.toString(), toPercent(defDownValue))
            return """$textDamage
                |$textStun
                |$textDefDown""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.w_4 }
    override val commentResId by lazy { return@lazy R.raw.w_spell4 }

    override val cost = 30
    override var callDown = 30
    override var mDamage = 0.45
    override var controlDuration = 3.0
    private val defDownDuration = 4.0
    private val defDownValue = 0.25


    override var actionEvent = ActionSpell { target, initiator ->
        initiator.damageEvent(mDamage * initiator.damage, target, this)
        initiator.rage -= cost
        target.effects.add(StunEffect(EffectThunderstorm(context), controlDuration, true).start(target))
        target.effects.add(DefenseDownEffect(context, defDownDuration, false, defDownValue).start(target))
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.rage >= cost && existTarget(unit)
    }
}

class SpellMadness : Spell(CostType.RAGE), SpellBurst {
    override fun loadView(context: Context) {
        view = SpellMadness(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_madness)
        }

    override val description: String
        get() {
            val textRes = context.getString(R.string.part_rage)
            val textAccumulate = context.getString(R.string.part_accumulate, rRage, textRes)
            val textBurst = context.getString(R.string.spell_burst, burstDuration.toInt(), toPercent(mDamage), toPercent(mDelayValue))
            return """$textBurst
                |$textAccumulate""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.w_5 }
    override val commentResId by lazy { return@lazy R.raw.w_spell5 }
    override val cost = 10
    override var callDown = 90
    private val mDamage = 0.6
    override var burstDuration = 12.0
    private val mDelayValue = 0.5
    private val rRage = 5

    override var actionEvent = ActionSpell { _, initiator ->
        initiator.rage += 10
        val action: ActionEffect = object : ActionEffect {
            override fun put(holder: Unit, initiator: Unit) {
                initiator.agility += 10
                initiator.force += 10
                initiator.multipleDamage += mDamage
            }

            override fun tick(count: Int, holder: Unit, initiator: Unit) {
                initiator.rage += rRage
            }

            override fun remove(holder: Unit, initiator: Unit) {
                initiator.agility -= 10
                initiator.force -= 10
                initiator.multipleDamage -= mDamage
            }
        }
        val effect = BurstEffect(EffectMadness(context), burstDuration, false)
        effect.actionEffects.add(action)
        initiator.effects.add(effect.start(initiator))
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.rage >= cost && unit.isSpeaking
    }
}

//endregion warrior

//region hunter
class SpellLightningBolt : Spell(CostType.MANA), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellLightningBolt(context)
    }

    override val description: String
        get() {
            return context.getString(R.string.spell_dam, toPercent(mDamage))
        }
    override val name: String
        get() {
            return context.getString(R.string.spell_lightning_bolt)
        }
    override val soundResId by lazy { return@lazy R.raw.h_1 }
    override val cost = 30
    override var callDown = 3
    override var mDamage: Double = 2.76

    override var actionEvent = ActionSpell { target, initiator ->
        initiator.mana -= cost
        initiator.damageEvent(mDamage * initiator.damage, target, this)
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.mana >= cost && unit.isArming && existTarget(unit) && unit.isSpeaking
    }
}

class SpellTrap : Spell(CostType.ENERGY), SpellCC {
    override fun loadView(context: Context) {
        view = SpellTrap(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_trap)
        }

    override val description: String
        get() {
            return context.getString(R.string.spell_stun, controlDuration.toString())
        }
    override val soundResId by lazy { return@lazy R.raw.h_2 }
    override val commentResId by lazy { return@lazy R.raw.h_spell2 }
    override val cost = 25
    override var callDown = 15
    override var controlDuration = 4.0

    override var actionEvent = ActionSpell { target, initiator ->
        target.effects.add(StunEffect(EffectTrap(context), controlDuration, false).start(target, initiator))
        initiator.energy -= cost
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellPiercingShot : Spell(CostType.ENERGY), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellPiercingShot(context)
    }

    override val description: String
        get() {
            return context.getString(R.string.spell_dam, toPercent(mDamage))
        }
    override val name: String
        get() {
            return context.getString(R.string.spell_name_piercing_shot)
        }
    override val soundResId by lazy { return@lazy R.raw.h_3 }
    override val cost = 50
    override var callDown = 5
    override var mDamage: Double = 5.0
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.energy -= cost
        initiator.damageEvent(mDamage * initiator.damage, target, this)
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellVengeful : Spell(CostType.ENERGY), SpellBurst {
    override fun loadView(context: Context) {
        view = SpellVengeful(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_vengeful)
        }

    override val description: String
        get() {
            val textResEnergy = context.getString(R.string.part_energy)
            val textResMana = context.getString(R.string.part_mana)
            val textAccumulateEnergy = context.getString(R.string.part_accumulate, rEnergy, textResEnergy)
            val textAccumulateMana = context.getString(R.string.part_accumulate, rMana, textResMana)
            val textBurst = context.getString(R.string.spell_burst, burstDuration.toInt(), toPercent(mDamage), toPercent(mDelayValue))
            return """$textBurst
                |$textAccumulateEnergy
                |$textAccumulateMana""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.h_4 }
    override val commentResId by lazy { return@lazy R.raw.h_spell4 }
    override val cost = 25
    override var callDown = 45
    private val mDamage = 0.45
    override var burstDuration = 4.0
    private val mDelayValue = 0.5
    private val rEnergy = 5
    private val rMana = 5

    override var actionEvent = ActionSpell { _, initiator ->
        val action: ActionEffect = object : ActionEffect {
            override fun put(holder: Unit, initiator: Unit) {
                initiator.agility += 20
                initiator.multipleDamage += mDamage
            }

            override fun tick(count: Int, holder: Unit, initiator: Unit) {
                initiator.energy += rEnergy
                initiator.mana += rMana
            }

            override fun remove(holder: Unit, initiator: Unit) {
                initiator.agility -= 20
                initiator.multipleDamage -= mDamage
            }
        }
        val effect = BurstEffect(EffectVengeful(context), burstDuration, false)
        effect.actionEffects.add(action)
        initiator.effects.add(effect.start(initiator))
        initiator.energy -= cost


    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost
    }
}

class SpellAuraOfNature : Spell(CostType.MANA), SpellDefence {
    override fun loadView(context: Context) {
        view = SpellAuraOfNature(context)
    }

    override val description: String
        get() {
            return context.getString(R.string.spell_def_u, defUpDuration.toInt(), toPercent(defUpValue))
        }
    override val name: String
        get() {
            return context.getString(R.string.spell_name_aura_of_nature)
        }
    override val soundResId by lazy { return@lazy R.raw.h_5 }
    override val commentResId by lazy { return@lazy R.raw.h_spell5 }

    override val cost = 50
    override var callDown = 60
    override var defUpDuration = 8.0
    override var defUpValue = 0.5

    override var actionEvent = ActionSpell { _, initiator ->
        initiator.effects.add(DefenseUpEffect(context, defUpDuration, true, defUpValue).start(initiator))
        initiator.mana -= cost


    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.mana >= cost && unit.isSpeaking
    }
}

//endregion

//region mage
class SpellWitchcraft : Spell(CostType.MANA), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellWitchcraft(context)
        effect = WitchcraftEffect(context, 13.0, maxCount)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_witchcraft)
        }

    override val description: String
        get() {
            val textEffect = context.getString(R.string.spell_witchcraft, maxCount)
            val textDamage = context.getString(R.string.spell_dam_witchcraft, toPercent(mDamage))
            return """$textDamage
                |$textEffect""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.m_1 }
    override val cost: Int = 15
    override var callDown: Int = 0
    override var mDamage: Double = 0.15
    private val maxCount = 5
    @Transient
    lateinit var effect: Effect
    override var actionEvent: ActionSpell = ActionSpell { target, initiator ->
        initiator.mana -= cost
        initiator.damageEvent(initiator.damage * mDamage * effect.currentCount, target, this)
        fun dampRest() {
            initiator.spells[SPELL_5].rest -= 3
            initiator.spells[SPELL_5].view.getSkillIcon().rest -= 3
            initiator.spells[SPELL_5].view.getSkillIcon().callDown -= 3
        }

        if (!initiator.effects.contains(effect)) {
            initiator.effects.add(effect.start(initiator))
            dampRest()
        } else {
            if (Random().nextBoolean()) {
                effect.currentCount += 2
                dampRest()
                dampRest()
            } else {
                effect.currentCount++
                dampRest()
            }
        }
    }

    override fun canUse(unit: Unit): Boolean {
        var addingCondition = true
        (0 until unit.effects.size)
                .filter { unit.effects[it] is WitchcraftEffect }
                .forEach { addingCondition = unit.effects[it].currentCount < unit.effects[it].maxCount }
        return super.canUse(unit) && unit.mana >= cost && unit.isSpeaking && addingCondition && existTarget(unit)
    }
}

class SpellSilence : Spell(CostType.MANA), SpellCC {
    override fun loadView(context: Context) {
        view = SpellSilence(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_silence)
        }

    override val description: String
        get() {
            val textSilence = context.getString(R.string.spell_silence, controlDuration.toString())
            val textDefDown = context.getString(R.string.spell_def_down_mage, defDownDuration.toString(), toPercent(defDownValue))
            return """$textSilence
                |$textDefDown""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.m_2 }
    override val commentResId by lazy { return@lazy R.raw.m_spell2 }
    override val cost = 20
    override var callDown = 40
    override var controlDuration = 4.0
    private val defDownDuration = 2.5
    private val defDownValue = 0.1
    override var actionEvent = ActionSpell { target, initiator ->
        var count = 0
        (0 until initiator.effects.size)
                .filter { initiator.effects[it] is WitchcraftEffect }
                .forEach {
                    count = initiator.effects[it].currentCount
                    initiator.effects[it].dispersion()
                }
        target.effects.add(DefenseDownEffect(context, (1 + count) * defDownDuration, false, defDownValue * (1 + count)).start(target))
        controlDuration = if (!target.isControl) {
            controlDuration + controlDuration * 0.6
        } else {
            controlDuration
        }
        target.effects.add(SilenceEffect(EffectSilence(context), controlDuration, true).start(target))
        controlDuration = 4.0
        initiator.mana -= cost
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.mana >= cost && existTarget(unit) && unit.isSpeaking
    }
}

class SpellFrostBolt : Spell(CostType.MANA), SpellCC, SpellDamage {
    override fun loadView(context: Context) {
        view = SpellFrostBolt(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_frost_bolt)
        }

    override val description: String
        get() {
            val textDamage = context.getString(R.string.spell_witchcraft_damage, toPercent(mDamage), toPercent(localMultipleDamage * mDamage))

            val textStun = context.getString(R.string.spell_stun, controlDuration.toString())
            return """$textDamage
                |$textStun""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.m_3 }
    override val commentResId by lazy { return@lazy R.raw.m_spell3 }
    override val cost = 60
    override var callDown = 36
    override var controlDuration = 3.7
    private val localMultipleDamage = 0.15
    override var mDamage = 3.0
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.mana -= cost
        target.effects.add(StunEffect(EffectFreezing(context), controlDuration, true).start(target))

        var count = 0
        (0 until initiator.effects.size)
                .filter { initiator.effects[it] is WitchcraftEffect }
                .forEach {
                    count = initiator.effects[it].currentCount
                    initiator.effects[it].dispersion()
                }

        initiator.damageEvent(initiator.damage * mDamage * (1 + count * localMultipleDamage), target, this)
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.mana >= cost && existTarget(unit) && unit.isSpeaking
    }
}

class SpellDistributionShield : Spell(CostType.MANA), SpellDefence {
    override fun loadView(context: Context) {
        view = SpellDistributionShield(context)
    }

    override val description: String
        get() {
            return context.getString(R.string.spell_def_u, defUpDuration.toInt(), toPercent(defUpValue))
        }
    override val name: String
        get() {
            return context.getString(R.string.spell_name_distribution_shield)
        }
    override val soundResId by lazy { return@lazy R.raw.m_4 }
    override val commentResId by lazy { return@lazy R.raw.m_spell4 }
    override val cost: Int = 45
    override var callDown: Int = 50
    lateinit var effect: Effect
    override var defUpDuration = 4.0
    override var defUpValue = 1.0
    override var actionEvent = ActionSpell { _, initiator ->
        initiator.effects.add(DefenseUpEffect(context, defUpDuration, false, defUpValue).start(initiator))
        initiator.mana -= cost
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.mana >= cost && existTarget(unit) && unit.isSpeaking
    }
}

class SpellBlast : Spell(CostType.MANA), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellBlast(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_blast)
        }

    override val description: String
        get() {
            return context.getString(R.string.spell_witchcraft_damage, toPercent(mDamage), toPercent(localMultipleDamage * mDamage))
        }
    override val soundResId by lazy { return@lazy R.raw.m_5 }
    override val commentResId by lazy { return@lazy R.raw.m_spell5 }
    override val cost = 90
    override var callDown = 42
    override var mDamage = 9.0
    private val localMultipleDamage = 0.12
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.mana -= cost
        var count = 0
        (0 until initiator.effects.size)
                .filter { initiator.effects[it] is WitchcraftEffect }
                .forEach {
                    count = initiator.effects[it].currentCount
                    initiator.effects[it].dispersion()
                }
        initiator.damageEvent(initiator.damage * mDamage * (1 + count * localMultipleDamage), target, this)
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.mana >= cost && existTarget(unit) && unit.isSpeaking
    }
}
//endregion

//region rogue

class SpellDaggerThrow : Spell(CostType.ENERGY), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellDaggerThrow(context)
    }

    override val description: String
        get() {
            return context.getString(R.string.spell_dam, toPercent(mDamage))
        }
    override val name: String
        get() {
            return context.getString(R.string.spell_name_dagger_throw)
        }

    override val soundResId by lazy { return@lazy R.raw.r_1 }
    override val cost = 5
    override var callDown = 0
    override var mDamage: Double = 0.5
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.damageEvent(initiator.damage * mDamage, target, this)
        initiator.energy -= cost
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellSuddenBlow : Spell(CostType.ENERGY), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellSuddenBlow(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_sudden_blow)
        }

    override val description: String
        get() {
            val textDamage = context.getString(R.string.spell_dam, toPercent(mDamage))
            val value = toPercent(defDownValue)
            val duration = (defDownDuration).toString()
            val textDefDown = context.getString(R.string.spell_def_d, duration, value)
            return """$textDamage
                |$textDefDown""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.r_2 }
    override val cost = 40
    override var callDown = 4
    override var mDamage: Double = 2.84
    private val defDownValue: Double = 0.17
    private val defDownDuration: Double = 3.5
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.damageEvent(initiator.damage * mDamage, target, this)
        initiator.energy -= cost
        target.effects.add(DefenseDownEffect(context, defDownDuration, false, defDownValue).start(target))

    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellSmoke : Spell(CostType.ENERGY) {
    override fun loadView(context: Context) {
        view = SpellSmoke(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_smoke)
        }

    override val description: String
        get() {
            return context.getString(R.string.spell_smoke, duration.toInt())
        }
    override val soundResId by lazy { return@lazy R.raw.r_3 }
    override val commentResId by lazy { return@lazy R.raw.r_spell2 }
    override val cost = 15
    override var callDown = 20
    private val duration = 2.6

    override var actionEvent = ActionSpell { _, initiator ->
        initiator.effects.add(SmokeEffect(context, duration, false).start(initiator))
        initiator.energy -= cost
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost
    }
}

class SpellPoisoning : Spell(CostType.ENERGY), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellPoisoning(context)
        effect = PoisingEffect(context, duration, mDotDamage, maxCount)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_poisoning)
        }

    override val description: String
        get() {
            val textPoising = context.getString(R.string.spell_dot, duration.toInt(), toPercent(mDotDamage), maxCount)
            val textDamage = context.getString(R.string.spell_dam, toPercent(mDamage))
            return """$textDamage
                |$textPoising""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.r_4 }
    override val cost = 40
    override var callDown = 6
    override var mDamage = 1.5
    private val duration = 12.0
    private val mDotDamage = 0.10
    private val maxCount = 5
    @Transient
    lateinit var effect: Effect
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.damageEvent(initiator.damage * mDamage, target, this)
        initiator.energy -= cost

        var effectFound = false
        target.effects.forEach {
            if (it.javaClass == effect.javaClass) {
                it.currentCount++
                effectFound = true
                return@forEach
            }
        }
        if (!effectFound) {
            target.effects.add(effect.copy().start(target, initiator))
        }
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellMastery : Spell(CostType.ENERGY), SpellBurst {
    override fun loadView(context: Context) {
        view = SpellMastery(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_mastery)
        }

    override val description: String
        get() {
            val textResEnergy = context.getString(R.string.part_energy)
            val textAccumulateEnergy = context.getString(R.string.part_accumulate, rEnergy, textResEnergy)
            val textBurst = context.getString(R.string.spell_burst, burstDuration.toInt(), toPercent(mDamage), toPercent(mAttackDelay))
            return """$textBurst
                |$textAccumulateEnergy""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.r_5 }
    override val commentResId by lazy { return@lazy R.raw.r_spell5 }
    override val cost = 10
    override var callDown = 60
    private val mDamage = 0.8
    private val rEnergy = 10
    override var burstDuration = 6.0
    private val mAttackDelay = 0.6
    override var actionEvent = ActionSpell { _, initiator ->
        val action: ActionEffect = object : ActionEffect {
            override fun put(holder: Unit, initiator: Unit) {
                initiator.multipleDamage += mDamage
                initiator.agility += 20
            }

            override fun tick(count: Int, holder: Unit, initiator: Unit) {
                initiator.energy += rEnergy
            }

            override fun remove(holder: Unit, initiator: Unit) {
                initiator.multipleDamage -= mDamage
                initiator.agility -= 20
            }
        }
        val effect = BurstEffect(EffectMastery(context), burstDuration, true)
        effect.actionEffects.add(action)
        initiator.effects.add(effect.start(initiator))
        initiator.energy -= cost
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost
    }
}

//endregion rogue

//region paladin

class SpellCrushingBlow : Spell(CostType.RAGE), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellCrushingBlow(context)
    }

    override val description: String
        get() {
            return context.getString(R.string.spell_dam, toPercent(mDamage))
        }
    override val name: String
        get() {
            return context.getString(R.string.spell_name_crushing_blow)
        }
    override val soundResId by lazy { return@lazy R.raw.p_1 }
    override val cost = 20
    override var callDown = 2
    override var mDamage = 2.5
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.damageEvent(initiator.damage * mDamage, target, this)
        initiator.rage -= cost
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.rage >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellFlash : Spell(CostType.MANA), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellFlash(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_flash)
        }

    override val description: String
        get() {
            return context.getString(R.string.spell_dam_executor, toPercent(mDamage), toPercent(pHp), toPercent(executorDamage / 2))
        }
    override val soundResId by lazy { return@lazy R.raw.p_2 }
    override val cost = 40
    override var callDown = 6
    override var mDamage = 4.0
    private val pHp = 0.1
    private val executorDamage = 2.0
    override var actionEvent = ActionSpell { target, initiator ->
        val e = if (target.hp <= target.maxHp * pHp) {
            executorDamage
        } else {
            1.0
        }
        initiator.damageEvent(initiator.damage * mDamage * e, target, this)
        initiator.mana -= cost
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.mana >= cost && existTarget(unit) && unit.isSpeaking
    }
}

class SpellHolyAura : Spell(CostType.MANA), SpellBurst, SpellHeal, SpellDefence {
    override fun loadView(context: Context) {
        view = SpellHolyAura(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_holy_aura)
        }

    override val description: String
        get() {
            return context.getString(R.string.spell_holy_aura, auraDuration.toInt(), toPercent(mDamage), rMana, toPercent(healMaxHp))
        }
    override val soundResId by lazy { return@lazy R.raw.p_3 }
    override val cost = 60
    override var callDown = 45
    private val auraDuration = 7.0
    override var burstDuration: Double = auraDuration
    private val mDamage = 0.4
    override var defUpDuration = auraDuration
    override var defUpValue = 0.2
    private val healMaxHp = 0.02
    private val rMana = 10
    override var mHeal = healMaxHp
    override val commentResId by lazy { return@lazy R.raw.p_spell3 }
    override var actionEvent = ActionSpell { _, initiator ->
        val action: ActionEffect = object : ActionEffect {
            override fun put(holder: Unit, initiator: Unit) {
                initiator.multipleDamage += mDamage
                initiator.mind += 10
                initiator.force += 10
                initiator.def += defUpValue
            }

            override fun tick(count: Int, holder: Unit, initiator: Unit) {
                initiator.mana += rMana
            }

            override fun remove(holder: Unit, initiator: Unit) {
                initiator.multipleDamage -= mDamage
                initiator.mind -= 10
                initiator.force -= 10
                initiator.def -= defUpValue
                initiator.healSelfEvent(initiator.maxHp * mHeal, this@SpellHolyAura)
            }
        }
        val effect = BurstEffect(EffectHolyAura(context), auraDuration, true)
        effect.actionEffects.add(action)
        initiator.effects.add(effect.start(initiator))
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.isSpeaking
    }
}

class SpellShieldBash : Spell(CostType.RAGE), SpellCC {
    override fun loadView(context: Context) {
        view = SpellShieldBash(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_shield_bash)
        }

    override val description: String
        get() {
            val textStun = context.getString(R.string.spell_stun, controlDuration.toString())
            val textDefDown = context.getString(R.string.spell_def_d, defDownDuration.toString(), toPercent(defDownValue))
            return """$textStun
                |$textDefDown""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.p_4 }
    override val commentResId by lazy { return@lazy R.raw.p_spell4 }
    override val cost = 35
    override var callDown = 45
    override var controlDuration = 4.0
    private val defDownDuration = 5.0
    private val defDownValue = 0.15
    override var actionEvent = ActionSpell { target, initiator ->
        target.effects.add(StunEffect(EffectStun(context), controlDuration, true).start(target, initiator))
        target.effects.add(DefenseDownEffect(context, defDownDuration, false, defDownValue).start(target, initiator))
        initiator.rage -= cost
        initiator.mana += 10
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.rage >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellHealing : Spell(CostType.MANA), SpellHeal {
    override fun loadView(context: Context) {
        view = SpellHealing(context)
        hotEffect = HealingEffect(context, hotDuration, mHot, hotMaxCount)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_healing)
        }

    override val description: String
        get() {
            return context.getString(R.string.spell_healing, toPercent(mHeal), hotDuration.toString(), toPercent(mHot), hotMaxCount)
        }
    override val soundResId by lazy { return@lazy R.raw.p_5 }
    override val cost = 20
    override var callDown = 0
    private val hotDuration = 9.0
    private val hotMaxCount = 5
    override var mHeal = 0.25
    private val mHot = 0.05
    @Transient
    lateinit var hotEffect: Effect
    override var actionEvent = ActionSpell { _, initiator ->
        initiator.healSelfEvent(initiator.damage * mHeal + initiator.mind, this)
        initiator.mana -= cost

        if (!initiator.effects.contains(hotEffect)) {
            initiator.effects.add(hotEffect.start(initiator))
        } else {
            hotEffect.currentCount++
        }
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.mana >= cost && unit.isSpeaking
    }
}
//endregion

//region berserker
class SpellBerserk : Spell(CostType.RAGE), SpellBurst {
    override fun loadView(context: Context) {
        view = SpellBerserk(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_rage)
        }

    override val description: String
        get() {
            return context.getString(R.string.spell_rage, burstDuration.toInt(), toPercent(mDamage), toPercent(defDownValue), bonusRage.toString())
        }
    override val soundResId by lazy { return@lazy R.raw.b_1 }
    override val cost = 0
    override var callDown = 45
    override var burstDuration: Double = 4.0
    private val defDownValue = 0.30
    private val mDamage = 0.25
    private val bonusRage = 30.0
    override val commentResId by lazy { return@lazy R.raw.b_spell1 }
    override var actionEvent = ActionSpell { _, initiator ->
        val action = object : ActionEffect {
            override fun put(holder: Unit, initiator: Unit) {
                initiator.def -= defDownValue
                initiator.multipleDamage += mDamage
                initiator.agility += 20
            }

            override fun tick(count: Int, holder: Unit, initiator: Unit) {
                initiator.def += defDownValue / burstDuration
                initiator.multipleDamage -= mDamage / burstDuration
                initiator.agility -= 5
            }

            override fun remove(holder: Unit, initiator: Unit) {
                initiator.def += defDownValue / burstDuration
                initiator.multipleDamage -= mDamage / burstDuration
                initiator.agility -= 5
            }
        }
        initiator.rage += bonusRage
        initiator.effects.filter { it.isDispel }.forEach { it.dispersion() }
        val effect = BurstEffect(EffectBerserk(context), burstDuration, false)
        effect.actionEffects.add(action)
        initiator.effects.add(effect.start(initiator))
    }

    override fun canUse(unit: Unit): Boolean {
        return true
    }
}

class SpellDisarming : Spell(CostType.ENERGY), SpellCC, SpellDamage {
    override fun loadView(context: Context) {
        view = SpellDisarming(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_disarming)
        }

    override val description: String
        get() {
            val partDamage = context.getString(R.string.spell_dam, toPercent(mDamage))
            val partDisarming = context.getString(R.string.spell_disarming, controlDuration.toString())
            val partDispel = context.getString(R.string.spell_dispel_mana_burn)
            return """$partDamage
                |$partDisarming
                |$partDispel
            """.trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.b_2 }
    override val commentResId by lazy { return@lazy R.raw.b_spell2 }
    override var mDamage: Double = 0.5
    override var controlDuration: Double = 2.5
    override val cost = 40
    override var callDown = 50
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.damageEvent(initiator.damage * mDamage, target, this)
        initiator.energy -= cost
        target.effects.forEach {
            if (it is ManaBurnEffect) {
                if (it.isDispel) {
                    it.dispersion()
                }
            }
        }
        target.effects.add(DisarmingEffect(EffectDestroyWeapon(context), controlDuration, true).start(target))
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellCripplingStrike : Spell(CostType.ENERGY), SpellDamage, SpellCC {
    override fun loadView(context: Context) {
        view = SpellCripplingStrike(context)
        stunEffect = StunEffect(EffectStun(context), controlDuration, true)
        silenceEffect = SilenceEffect(EffectSilence(context), silenceDuration, true)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_crippling_blow)
        }

    override val description: String
        get() {
            val partDamage = context.getString(R.string.spell_dam, toPercent(mDamage))
            val partStun = context.getString(R.string.spell_stun, controlDuration.toString())
            val partSilence = context.getString(R.string.spell_crippling_blow, silenceDuration.toString())
            return """$partDamage
                |$partStun
                |$partSilence""".trimMargin()
        }
    override val soundResId by lazy { return@lazy R.raw.b_3 }

    override val cost = 50
    override var callDown = 35
    override var mDamage: Double = 0.5
    override var controlDuration: Double = 2.7
    private val silenceDuration: Double = 1.3
    private lateinit var onMoveEffectListener: OnMoveEffectsListener
    lateinit var stunEffect: Effect
    lateinit var silenceEffect: Effect
    override var actionEvent = ActionSpell { target, initiator ->
        onMoveEffectListener = object : OnMoveEffectsListener {
            override fun onPut(effect: Effect?) {

            }

            override fun onRemove(effect: Effect?) {
                if (effect == stunEffect) {
                    target.effects.add(silenceEffect.start(target))
                    target.onMoveEffectsListeners.remove(onMoveEffectListener)
                }
            }
        }
        target.onMoveEffectsListeners.add(onMoveEffectListener)
        initiator.damageEvent(initiator.damage * mDamage, target, this)
        initiator.energy -= cost
        target.effects.add(stunEffect.start(target))
    }


    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellLunge : Spell(CostType.RAGE), SpellDamage, SpellHeal {
    override fun loadView(context: Context) {
        view = SpellLunge(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_rage_lunge)
        }

    override val description: String
        get() {
            val partDamage = context.getString(R.string.spell_dam, toPercent(mDamage))
            val partDefDown = context.getString(R.string.spell_def_d, mDefDownDuration.toString(), toPercent(mDefDownValue))
            val partLifeSteal = context.getString(R.string.spell_life_steal, toPercent(mHeal))
            return """$partDamage
                |$partDefDown
                |$partLifeSteal""".trimMargin()

        }
    override val soundResId by lazy { return@lazy R.raw.b_4 }
    override val cost = 30
    override var callDown = 7
    override var mDamage: Double = 2.4
    private val mDefDownDuration: Double = 2.35
    private val mDefDownValue: Double = 0.60
    override var mHeal = 0.25
    override var actionEvent = ActionSpell { target, initiator ->
        var mRageHealing = 1
        initiator.effects.forEach {
            if (it is BurstEffect) {
                mRageHealing = 4
            }
        }
        val heal = mHeal * initiator.damageEvent(initiator.damage * mDamage * mRageHealing, target, this)
        initiator.healSelfEvent(heal, this)
        initiator.rage -= cost
        target.effects.add(DefenseDownEffect(context, mDefDownDuration, true, mDefDownValue).start(target, initiator))
    }


    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.rage >= cost && unit.isArming && existTarget(unit)
    }
}

class SpellManaBurn : Spell(CostType.ENERGY), SpellDamage {
    override fun loadView(context: Context) {
        view = SpellManaBurn(context)
        effect = ManaBurnEffect(context, burnDuration, burnValue, mDotDamage, maxCount)
    }

    override val description: String
        get() {
            val partDamage = context.getString(R.string.spell_dam, toPercent(mDamage))
            val partManaBurn = context.getString(R.string.spell_mana_burn, burnDuration.toString(), burnValue.toString(), toPercent(mDotDamage), maxCount)
            return """$partDamage
                |$partManaBurn""".trimMargin()
        }

    override val name: String
        get() {
            return context.getString(R.string.spell_name_mana_burn)
        }

    override val soundResId by lazy { return@lazy R.raw.b_5 }
    override val cost = 60
    override var callDown = 6
    override var mDamage = 2.0
    private val mDotDamage = 0.12
    private val burnValue: Double = 0.2
    private val burnDuration: Double = 10.0
    private val maxCount: Int = 10
    @Transient
    lateinit var effect: Effect
    override var actionEvent = ActionSpell { target, initiator ->
        initiator.damageEvent(initiator.damage * mDamage, target, this)
        initiator.energy -= cost
        var effectFound = false
        target.effects.forEach {
            if (it.javaClass == effect.javaClass) {
                it.currentCount++
                effectFound = true
                return@forEach
            }
        }
        if (!effectFound) {
            target.effects.add(effect.copy().start(target, initiator))
        }
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && unit.energy >= cost && unit.isArming && existTarget(unit)
    }
}

//endregion
//region t1
class SpellT1MagicShield : Spell(CostType.MANA) {
    override fun loadView(context: Context) {
        view = SpellT1MagicShield(context)
    }

    override val description: String
        get() {
            return context.getString(R.string.t1_magic_shield_desc)
        }

    override val name: String
        get() {
            return context.getString(R.string.t1_magic_shield)
        }

    override val cost: Int = 0
    override var callDown = 90
    override var actionEvent: ActionSpell = ActionSpell { _, initiator ->
        val effect = PositiveEffect(EffectMagicShield(context), 3.0, false)
        effect.actionEffects.add(object : ActionEffect {
            fun dispel(effect: Effect) {
                if (effect.isDispel) {
                    effect.dispersion()
                }
            }

            override fun put(holder: Unit, initiator: Unit) {
                holder.effects.forEach {
                    if (it is NegativeEffect) {
                        dispel(it)
                    }
                }
            }

            override fun tick(count: Int, holder: Unit, initiator: Unit) {
                holder.effects.forEach {
                    if (it is NegativeEffect) {
                        dispel(it)
                    }
                }
            }

            override fun remove(holder: Unit, initiator: Unit) {
                holder.effects.forEach {
                    if (it is NegativeEffect) {
                        dispel(it)
                    }
                }
            }
        })
        initiator.effects.add(effect.start(initiator))
    }
}


class SpellT1DispelMagic : Spell(CostType.MANA) {
    override fun loadView(context: Context) {
        view = SpellT1DispelMagic(context)
    }

    override val description: String
        get() {
            return context.getString(R.string.t1_dispel_magic_desc)
        }

    override val name: String
        get() {
            return context.getString(R.string.t1_dispel_magic)
        }

    override val cost = 0

    override var callDown = 90
    override var actionEvent: ActionSpell = ActionSpell { target, _ ->
        target.effects.forEach {
            if (it is PositiveEffect && it.isDispel) {
                it.dispersion()
            }
        }
    }

    override fun canUse(unit: Unit): Boolean {
        return super.canUse(unit) && existTarget(unit)
    }
}

class SpellT1Freedom : Spell(CostType.MANA) {
    override fun loadView(context: Context) {
        view = SpellT1Freedom(context)
    }

    override val name: String
        get() {
            return context.getString(R.string.t1_freedom)
        }

    override val description: String
        get() {
            return context.getString(R.string.t1_freedom_desc)
        }

    override val commentResId by lazy { return@lazy R.raw.freedom }
    override val cost: Int = 0
    override var callDown: Int = 90
    override var actionEvent = ActionSpell { _, initiator ->
        initiator.effects.forEach {
            if (it is NegativeEffect && it.isDispel) {
                it.dispersion()
            }
        }
    }

    override fun canUse(unit: Unit): Boolean {
        return true
    }
}
//endregion

//region flasks
class SpellManaFlask : Spell(CostType.MANA) {
    override fun loadView(context: Context) {
        view = SpellManaFlask(context)
        view.count = count
    }

    override val name: String
        get() = ""
    override val description: String
        get() = ""

    override val cost: Int = 0
    override var callDown: Int = 15
    override var actionEvent = ActionSpell { _, initiator ->
        initiator.mana += 70
        initiator.flaskMana!!.count--
        count--
        view.count = initiator.flaskMana!!.count
        if (initiator.flaskMana!!.count == 0) {
            initiator.flaskMana = null
        }
    }

    override fun canUse(unit: Unit): Boolean {
        return true
    }
}

class SpellHpFlask : Spell(CostType.MANA), SpellHeal {
    override fun loadView(context: Context) {
        view = SpellHpFlask(context)
        view.count = count
    }

    override val name: String
        get() = ""
    override val description: String
        get() = ""

    override var mHeal: Double = 0.1
    override val cost: Int = 0
    override var callDown: Int = 45
    override var actionEvent = ActionSpell { _, initiator ->
        initiator.healSelfEvent(initiator.maxHp * mHeal, this)
        initiator.flaskHp!!.count--
        count--
        view.count = initiator.flaskHp!!.count
        if (initiator.flaskHp!!.count == 0) {
            initiator.flaskHp = null
        }
    }

    override fun canUse(unit: Unit): Boolean {
        return true
    }
}
//endregion




