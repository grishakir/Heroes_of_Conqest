package com.thekingames.medivalwarriors2.core.model

import android.content.Context
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.interfaces.*
import com.thekingames.medivalwarriors2.core.model.shop.*
import com.thekingames.ui.*
import java.io.Serializable
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

const val SPELL_1 = 0
const val SPELL_2 = 1
const val SPELL_3 = 2
const val SPELL_4 = 3
const val SPELL_5 = 4
const val SpreadDamage = 0.03
const val AVOID = 0.6

const val maxRage = 100.0
const val maxEnergy = 100.0
const val maxMana = 100.0

const val minGCD = 0.5
const val minLCD = 0.35

val random = Random(System.currentTimeMillis())


abstract class Unit(var force: Int,
                    var agility: Int,
                    var mind: Int,
                    var stamina: Int,
                    var baseDamage: Double,
                    val baseRes: Double,
                    val nameId: Int
) : GameCycle, OnTickListener, Serializable {

    abstract val framesId: ArrayList<Int>

    //region hp
    val maxHp: Double
        get() = stamina * 35.0

    var hp: Double = maxHp
        set(value) {
            var newHp = value
            var damage = hp - newHp

            if (damage > 0) {
                damage *= (1 - getResultDefence())
                damage *= if (def < 1) {
                    (1 - def)
                } else {
                    0.0
                }
                newHp = hp - damage
            }

            field = if (newHp < maxHp) newHp else maxHp

            if (field <= 0) {
                field = 0.0
            }
            onHpChangeListeners.forEach { it.change() }
            onHpUpdateListeners.forEach { it.update(damage) }

            if (field == 0.0) {
                isControl = false
                isAlive = false
            }
        }

    var rHp: Double = force * 0.08
    //endregion

    //region dmg
    var multipleDamage: Double = 1.0
    var multipleHealing: Double = 1.0
        set(value) {
            field = if (value >= 0) {
                value
            } else {
                0.0
            }
        }
    var multipleHot: Double = 1.0
        set(value) {
            field = if (value >= 0) {
                value
            } else {
                0.0
            }
        }
    private val criticalChance: Double
        get() = if (agility.toDouble() * 0.5 <= 100) {
            agility.toDouble() * 0.5
        } else {
            100.0
        }
    private val criticalFactor: Double
        get() = 1 + force * 0.005

    val damage: Double = baseDamage
        get() {
            return if (random.nextInt(101) <= criticalChance) {
                val dam = field * multipleDamage * criticalFactor
                dam - SpreadDamage * (dam - random.nextInt((dam * 2).toInt()))
            } else {
                val dam = field * multipleDamage
                dam - SpreadDamage * (dam - random.nextInt((dam * 2).toInt()))
            }
        }
    //endregion

    //region def
    var def: Double = 0.0
    private val res: Double = baseRes

    private fun getResultDefence(): Double {
        return if (random.nextBoolean()) {
            AVOID
        } else {
            res
        }
    }
    //endregion

    //region rage
    var rage: Double = 0.0
        set(value) {
            field = if (value < maxRage) value else maxRage
            onRageChangeListeners.forEach { it.change() }
        }
    private val rRage: Double
        get() = force * 0.1
    //endregion

    //region energy
    var energy: Double = 100.0
        set(value) {
            field = if (value < maxEnergy) value else maxEnergy
            onEnergyChangeListeners.forEach { it.change() }
        }
    val rEnergy: Double
        get() = agility * 0.2
    //endregion

    //region mana
    var mana: Double = 100.0
        set(value) {
            field = if (value < maxMana) value else maxMana
            onManaChangeListeners.forEach { it.change() }
        }
    private val rMana: Double
        get() = mind * 0.2
    //endregion

    //region hast
    val gcd: Double
        get() = if (1.9 - agility * 0.016 >= minGCD) (1.9 - agility * 0.016) else minGCD

    val localCallDown: Double
        get() = if (1 - mind * 0.008 >= minLCD) (1 - mind * 0.008) else minLCD
    //endregion

    //region control
    var isAlive: Boolean = true
        set(value) {
            field = value
            onAliveChangeListeners.forEach { it.change() }
        }

    var isControl: Boolean = true
        set(value) {
            field = value
            releaseSpells()
            if (!value) {
                rage -= 10
            }
            onControlChangeListeners.forEach { it.change() }
        }
    var isSpeaking: Boolean = true
        set(value) {
            field = value
            onSpeakingChangeListeners.forEach { it.change() }
        }

    var isArming: Boolean = true
        set(value) {
            field = value
            onArmingChangeListeners.forEach { it.change() }
        }
    //endregion

    //region listener
    var onHpChangeListeners: ArrayList<OnChangeListener> = arrayListOf()
    var onRageChangeListeners: ArrayList<OnChangeListener> = arrayListOf()
    var onEnergyChangeListeners: ArrayList<OnChangeListener> = arrayListOf()
    var onManaChangeListeners: ArrayList<OnChangeListener> = arrayListOf()
    var onMoveEffectsListeners: ArrayList<OnMoveEffectsListener> = arrayListOf()
    var onAliveChangeListeners: ArrayList<OnChangeListener> = arrayListOf()
    var onControlChangeListeners: ArrayList<OnChangeListener> = arrayListOf()
    var onSpeakingChangeListeners: ArrayList<OnChangeListener> = arrayListOf()
    var onArmingChangeListeners: ArrayList<OnChangeListener> = arrayListOf()
    var onHpUpdateListeners: ArrayList<OnUpdateListener> = arrayListOf()
    var onSpellUseListeners: ArrayList<OnSpellUseListener> = arrayListOf()
    var onDamageListeners: ArrayList<OnDamageListener> = arrayListOf()
    var onHealListeners: ArrayList<OnHealListener> = arrayListOf()
    var onTickListeners: ArrayList<OnTickListener> = arrayListOf()
    var onUnitReleaseListeners: ArrayList<OnUnitReleaseListener> = arrayListOf()
    //endregion

    //region bars
    @Transient
    lateinit var bars: Array<TextBar>

    //endregion

    //region spells
    var spells: ArrayList<Spell> = arrayListOf()
    @Transient
    lateinit var spellsRotation: ArrayList<Spell>
    @Transient
    lateinit var spellsDefence: ArrayList<Spell>
    @Transient
    lateinit var spellsCC: ArrayList<Spell>
    @Transient
    lateinit var spellsBurst: ArrayList<Spell>
    //endregion

    //region effects
    @Transient
    lateinit var effects: CopyOnWriteArrayList<Effect>
    @Transient
    lateinit var stunDiminishing: Diminishing
    @Transient
    lateinit var disarmingDiminishing: Diminishing
    @Transient
    lateinit var silenceDiminishing: Diminishing
    @Transient
    var target: Unit? = null
        set(value) {
            field = value
            releaseSpells()
        }
    @Transient
    lateinit var focus: Unit
    //endregion

    //region items
    abstract val weapons: ArrayList<Item>

    var flaskHp: FlaskHpItem? = null
        set(value) {
            if (field == null && value != null) {
                value.apply(this)
            } else if (field != null && value == null) {
                field?.cancel(this)
            }
            field = value
        }
    var flaskMana: FlaskManaItem? = null
        set(value) {
            if (field == null && value != null) {
                value.apply(this)
            } else if (field != null && value == null) {
                field?.cancel(this)
            }
            field = value
        }

    fun resetWeapon(index: Int, weapon: Item) {
        weapons[index].cancel(this)
        weapons[index] = weapon
        weapons[index].apply(this)
    }

    //endregion

    //region exp
    var exp: Int = 0
        set(value) {
            var dExp = value - exp
            val needDExpForNextLvl = capExp - exp
            if (dExp < needDExpForNextLvl) {
                field = value
            } else {
                dExp -= needDExpForNextLvl
                field = 0
                lvl++
                exp += dExp
            }
        }
    val capExp: Int get() = (25 * (Math.pow(lvl.toDouble(), 2.0))).toInt()
    var points: Int = 3
        set(value) {
            field = if (points >= 0) {
                value
            } else {
                0
            }
        }
    @Transient
    var onLvlChangeListener: OnChangeListener? = null
    var lvl: Int = 1
        set(value) {
            if (field < maxLvl) {
                field = value
                onLvlChangeListener?.change()
                points++
            } else {
                field = maxLvl
            }
        }
    private val maxLvl: Int = 25
    //endregion

    protected fun sortSpells() {
        spellsBurst = arrayListOf()
        spellsCC = arrayListOf()
        spellsDefence = arrayListOf()
        for (spell: Spell in spells) {
            when (spell) {
                is SpellBurst -> spellsBurst.add(spell)
                is SpellCC -> spellsCC.add(spell)
                is SpellDefence -> spellsDefence.add(spell)
            }
        }
    }

    protected fun settingBars() {
        for (bar: TextBar in bars) {
            when (bar) {
                is TextBarHp -> {
                    onHpChangeListeners.add(OnChangeListener { bar.view.setValue(hp) })
                    bar.view.setMaxValue(maxHp.toInt())
                }
                is TextBarRage -> {
                    onRageChangeListeners.add(OnChangeListener { bar.view.setValue(rage);releaseSpells() })
                    bar.view.setMaxValue(100)
                }
                is TextBarEnergy -> {
                    onEnergyChangeListeners.add(OnChangeListener { bar.view.setValue(energy);releaseSpells() })
                    bar.view.setMaxValue(100)
                }
                is TextBarMana -> {
                    onManaChangeListeners.add(OnChangeListener { bar.view.setValue(mana);releaseSpells() })
                    bar.view.setMaxValue(100)
                }
            }
        }
    }

    abstract fun bindSpells(context: Context)
    abstract fun bindBars(context: Context, side: Side)

    fun release() {
        effects = CopyOnWriteArrayList()
        stunDiminishing = Diminishing()
        disarmingDiminishing = Diminishing()
        silenceDiminishing = Diminishing()
        hp = maxHp
        energy = maxEnergy
        rage = maxRage
        mana = maxMana
        isAlive = true
        isControl = true
        isSpeaking = true
        isArming = true
        def = 0.0
        multipleDamage = 1.0
        multipleHealing = 1.0
        multipleHot = 1.0
        spells.forEach { it.rest = 0.0;it.gRest = 0.0; }
        onUnitReleaseListeners.forEach { it.release() }
    }

    override fun onTick() {
        if (isAlive) {
            regeneration()
            spells.forEach { it.onTick() }
            effects.forEach { it.onTick() }
            onTickListeners.forEach { it.onTick() }
        }
    }


    //region game cycle
    override fun begin() {
        onControlChangeListeners.add(OnChangeListener { releaseSpells() })
        onSpeakingChangeListeners.add(OnChangeListener { releaseSpells() })
        onArmingChangeListeners.add(OnChangeListener { releaseSpells() })

        onTickListeners.add(stunDiminishing)
        onTickListeners.add(silenceDiminishing)
        onTickListeners.add(disarmingDiminishing)
    }


    override fun pause() {

    }

    override fun resume() {

    }

    override fun end() {
        for (i in 0 until effects.size) {
            effects[0].dispersion()
        }

        spells.forEach { it.rest = 0.0;it.gRest = 0.0;it.view.setRest(0.0);it.view.setGRest(0.0); }

        val cleaner = arrayListOf(onHpChangeListeners,
                onManaChangeListeners,
                onRageChangeListeners,
                onEnergyChangeListeners,
                onMoveEffectsListeners,
                onHpUpdateListeners,
                onControlChangeListeners,
                onSpellUseListeners,
                onMoveEffectsListeners,
                onArmingChangeListeners,
                onAliveChangeListeners,
                onDamageListeners,
                onHealListeners,
                onTickListeners)
        cleaner.forEach { it.clear() }
    }

    //endregion

    private fun regeneration() {
        healSelfEvent(rHp * 0.1)
        rage += rRage * 0.1
        energy += rEnergy * 0.1
        mana += rMana * 0.1
    }

    private fun releaseSpells() {
        for (i in 0 until spells.size) {
            if (spells[i].canUse(this)) {
                spells[i].view.activate()
            } else {
                spells[i].view.unActivate()
            }
        }
    }

    /**
     * @return result spell damage(netDamage)(absolutely value)
     */
    fun damageEvent(damage: Double, target: Unit, spell: SpellDamage): Double {
        val hp1 = target.hp
        target.hp -= damage
        val hp2 = target.hp
        val netDamage = hp1 - hp2       //netDamage > 0
        onDamageListeners.forEach { it.makeDamage(netDamage, spell) }
        return netDamage
    }

    /**
     * @return result dot effect damage(netDamage)(absolutely value)
     */
    fun damageEvent(damage: Double, target: Unit, effect: DOT): Double {
        val hp1 = target.hp
        target.hp -= damage
        val hp2 = target.hp
        val netDamage = hp1 - hp2       //netDamage > 0
        onDamageListeners.forEach { it.makeDamage(netDamage, effect) }
        return netDamage
    }


    private fun healSelfEvent(heal: Double) {
        val hp1 = hp
        hp += heal
        val hp2 = hp
        val netHeal = hp2 - hp1     //netHeal > 0
        onHealListeners.forEach { it.makeHeal(netHeal) }
    }


    fun healSelfEvent(heal: Double, spell: SpellHeal) {
        val hp1 = hp
        hp += heal * multipleHealing
        multipleHealing -= 0.05
        val hp2 = hp
        val netHeal = hp2 - hp1     //netHeal > 0
        if (netHeal != 0.0) {
            onHealListeners.forEach { it.makeHeal(netHeal, spell) }
        }
    }

    fun healSelfEvent(heal: Double, effect: HOT) {
        val hp1 = hp
        hp += heal * multipleHot
        multipleHot -= 0.01
        val hp2 = hp
        val netHeal = hp2 - hp1     //netHeal > 0
        if (netHeal > 0.0) {
            onHealListeners.forEach { it.makeHeal(netHeal, effect) }
        }
    }
    //endregion

    abstract fun load(context: Context)
}

open class Warrior : Unit(50, 11, 9, 45, 28.8, 0.46, R.string.warrior_name) {
    @Transient
    override var framesId: ArrayList<Int> = arrayListOf()
        get() = arrayListOf(R.drawable.w_0,
                R.drawable.w_1,
                R.drawable.w_2,
                R.drawable.w_3,
                R.drawable.w_4,
                R.drawable.w_5)


    override val weapons: ArrayList<Item> = arrayListOf(
            WarriorItem(1, 0, 100, R.drawable.sword, R.string.sword_w_name, TWO_HANDED))


    override fun load(context: Context) {
        if (spells.size == 0) {
            spells = arrayListOf(
                    SpellCuttingBlow(),
                    SpellReflection(),
                    SpellDeepWound(),
                    SpellThunderstorm(),
                    SpellMadness())
        }
        spells.forEach { it.load(context) }
    }

    override fun bindSpells(context: Context) {
        spellsRotation = arrayListOf(spells[SPELL_1], spells[SPELL_3])
        sortSpells()
    }

    override fun bindBars(context: Context, side: Side) {
        bars = if (side == Side.LEFT) {
            arrayOf(TextBarHp1(context),
                    TextBarRage1(context))
        } else {
            arrayOf(TextBarHp2(context),
                    TextBarRage2(context))
        }
        settingBars()
    }
}

open class Hunter : Unit(20, 25, 25, 35, 38.8, 0.44, R.string.hunter_name) {
    @Transient
    override var framesId: ArrayList<Int> = arrayListOf()
        get() = arrayListOf(R.drawable.h_0,
                R.drawable.h_1,
                R.drawable.h_2,
                R.drawable.h_3,
                R.drawable.h_4,
                R.drawable.h_5)

    override var weapons: ArrayList<Item> = arrayListOf(
            HunterItem(1, 0, 100, R.drawable.bow, R.string.bow_name, TWO_HANDED))

    override fun load(context: Context) {
        if (spells.isEmpty()) {
            spells = arrayListOf(
                    SpellLightningBolt(),
                    SpellTrap(),
                    SpellVengeful(),
                    SpellPiercingShot(),
                    SpellAuraOfNature()
            )
        }
        spells.forEach { it.load(context) }

    }

    override fun bindSpells(context: Context) {
        spellsRotation = arrayListOf(spells[SPELL_1], spells[SPELL_4])
        sortSpells()
    }

    override fun bindBars(context: Context, side: Side) {
        bars = if (side == Side.LEFT) {
            arrayOf(TextBarHp1(context),
                    TextBarEnergy1(context),
                    TextBarMana1(context))
        } else {
            arrayOf(TextBarHp2(context),
                    TextBarEnergy2(context),
                    TextBarMana2(context))
        }
        settingBars()
    }
}

class Mage : Unit(10, 10, 50, 30, 48.0, 0.40, R.string.mage_name) {
    @Transient
    override var framesId: ArrayList<Int> = arrayListOf()
        get() = arrayListOf(R.drawable.m_0,
                R.drawable.m_1,
                R.drawable.m_2,
                R.drawable.m_3,
                R.drawable.m_4,
                R.drawable.m_5)

    override var weapons: ArrayList<Item> = arrayListOf(
            MageItem(1, 0, 50, R.drawable.wand, R.string.wand_name, LEFT_HAND),
            MageItem(1, 0, 50, R.drawable.tome, R.string.tome_name, RIGHT_HAND))

    override fun load(context: Context) {
        if (spells.isEmpty()) {
            this.spells = arrayListOf(
                    SpellWitchcraft(),
                    SpellSilence(),
                    SpellFrostBolt(),
                    SpellDistributionShield(),
                    SpellBlast()
            )
        }
        spells.forEach { it.load(context) }

    }

    override fun bindSpells(context: Context) {
        spellsRotation = arrayListOf(spells[SPELL_1], spells[SPELL_2], spells[SPELL_5])
        sortSpells()
    }

    override fun bindBars(context: Context, side: Side) {
        bars = if (side == Side.LEFT) {
            arrayOf(TextBarHp1(context),
                    TextBarMana1(context))
        } else {
            arrayOf(TextBarHp2(context),
                    TextBarMana2(context))
        }
        settingBars()
    }
}

open class Rogue : Unit(12, 50, 8, 30, 45.0, 0.44, R.string.rogue_name) {
    @Transient
    override var framesId: ArrayList<Int> = arrayListOf()
        get() = arrayListOf(R.drawable.r_0,
                R.drawable.r_1,
                R.drawable.r_2,
                R.drawable.r_3,
                R.drawable.r_4,
                R.drawable.r_5)

    override var weapons: ArrayList<Item> = arrayListOf(
            RogueItem(1, 0, 50, R.drawable.dagger, R.string.dagger_name, ONE_HANDED),
            RogueItem(1, 0, 50, R.drawable.dagger, R.string.dagger_name, ONE_HANDED))

    override fun load(context: Context) {
        if (spells.isEmpty()) {
            spells = arrayListOf(
                    SpellDaggerThrow(),
                    SpellSuddenBlow(),
                    SpellSmoke(),
                    SpellPoisoning(),
                    SpellMastery()
            )
        }
        spells.forEach { it.load(context) }
    }

    override fun bindSpells(context: Context) {
        spellsRotation = arrayListOf(spells[SPELL_1], spells[SPELL_2], spells[SPELL_4])
        sortSpells()
    }

    override fun bindBars(context: Context, side: Side) {
        bars = if (side == Side.LEFT) {
            arrayOf(TextBarHp1(context),
                    TextBarEnergy1(context))
        } else {
            arrayOf(TextBarHp2(context),
                    TextBarEnergy2(context))
        }
        settingBars()
    }
}

class Paladin : Unit(25, 10, 35, 50, 26.0, 0.46, R.string.paladin_name) {
    @Transient
    override var framesId: ArrayList<Int> = arrayListOf()
        get() = arrayListOf(R.drawable.p_0,
                R.drawable.p_1,
                R.drawable.p_2,
                R.drawable.p_3,
                R.drawable.p_4,
                R.drawable.p_5)

    override var weapons: ArrayList<Item> = arrayListOf(
            PaladinItem(1, 0, 50, R.drawable.sword, R.string.sword_p_name, LEFT_HAND),
            PaladinItem(1, 0, 50, R.drawable.shield, R.string.shield_name, RIGHT_HAND))

    override fun load(context: Context) {
        if (spells.isEmpty()) {
            spells = arrayListOf(
                    SpellCrushingBlow(),
                    SpellFlash(),
                    SpellHolyAura(),
                    SpellShieldBash(),
                    SpellHealing()
            )
        }
        spells.forEach { it.load(context) }
    }

    override fun bindSpells(context: Context) {
        spellsRotation = arrayListOf(spells[SPELL_1], spells[SPELL_2], spells[SPELL_5])
        sortSpells()
    }

    override fun bindBars(context: Context, side: Side) {
        bars = if (side == Side.LEFT) {
            arrayOf(TextBarHp1(context),
                    TextBarRage1(context),
                    TextBarMana1(context))
        } else {
            arrayOf(TextBarHp2(context),
                    TextBarRage2(context),
                    TextBarMana2(context))
        }
        settingBars()
    }
}

class Berserker : Unit(25, 30, 15, 40, 40.0, 0.34, R.string.berserker_name) {
    @Transient
    override var framesId: ArrayList<Int> = arrayListOf()
        get() = arrayListOf(R.drawable.b_0,
                R.drawable.b_1,
                R.drawable.b_2,
                R.drawable.b_3,
                R.drawable.b_4,
                R.drawable.b_5)

    override var weapons: ArrayList<Item> = arrayListOf(
            BerserkItem(1, 0, 50, R.drawable.axe, R.string.axe1_name, ONE_HANDED),
            BerserkItem(1, 0, 50, R.drawable.axe, R.string.axe1_name, ONE_HANDED))

    override fun load(context: Context) {
        if (spells.isEmpty()) {
            spells = arrayListOf(
                    SpellBerserk(),
                    SpellDisarming(),
                    SpellCripplingStrike(),
                    SpellLunge(),
                    SpellManaBurn()
            )
        }
        spells.forEach { it.load(context) }

    }

    override fun bindSpells(context: Context) {
        spellsRotation = arrayListOf(spells[SPELL_2], spells[SPELL_4], spells[SPELL_5])
        sortSpells()
    }

    override fun bindBars(context: Context, side: Side) {
        bars = if (side == Side.LEFT) {
            arrayOf(TextBarHp1(context),
                    TextBarRage1(context),
                    TextBarEnergy1(context))
        } else {
            arrayOf(TextBarHp2(context),
                    TextBarRage2(context),
                    TextBarEnergy2(context))
        }
        settingBars()
    }
}