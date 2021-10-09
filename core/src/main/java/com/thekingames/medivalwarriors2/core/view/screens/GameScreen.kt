package com.thekingames.medivalwarriors2.core.view.screens

import android.animation.LayoutTransition
import android.media.AudioManager
import android.media.SoundPool
import android.text.format.DateFormat
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.controller.Controller
import com.thekingames.medivalwarriors2.core.controller.ai.AIController
import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.model.interfaces.*
import com.thekingames.medivalwarriors2.core.model.threads.LoopingThread
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.medivalwarriors2.core.view.activity.Settings.CHEAT_ENABLE
import com.thekingames.screenmanager.Screen
import kotlinx.android.synthetic.main.game1s.view.*
import java.util.*
import kotlin.collections.ArrayList


class GameScreen(var parent: ViewGroup, var controller1: Controller, var controller2: Controller) : Screen(parent, R.layout.game1s) {
    private lateinit var skillContainer1: LinearLayout
    private lateinit var skillContainer2: LinearLayout

    private var gameCycleListeners = ArrayList<GameCycle>()

    private lateinit var a: MainActivity
    private lateinit var soundPool: SoundPool

    private lateinit var gameThread: LoopingThread
    lateinit var statCollectorPlayer1: StatCollector
    lateinit var statCollectorPlayer2: StatCollector

    private lateinit var unit1: Unit
    private lateinit var unit2: Unit
    private lateinit var units: ArrayList<Unit>
    private lateinit var data1: ArrayList<Unit>
    private lateinit var unitsPlayer1: ArrayList<Unit>
    private lateinit var data2: ArrayList<Unit>
    private lateinit var unitsPlayer2: ArrayList<Unit>

    var speed = 1

    //region slots
    private lateinit var slotsPlayer1: ArrayList<RelativeLayout>
    private lateinit var slotsPlayer2: ArrayList<RelativeLayout>

    private lateinit var imagesPlayer1: ArrayList<ImageView>
    private lateinit var imagesPlayer2: ArrayList<ImageView>
    //endregion

    //region listeners
    private lateinit var onMoveEffectsListenerPlayer1: OnMoveEffectsListener
    private lateinit var onMoveEffectsListenerPlayer2: OnMoveEffectsListener

    private lateinit var onAliveListenerPlayer1: OnChangeListener
    private lateinit var onAliveListenerPlayer2: OnChangeListener

    private lateinit var onDamageListenerPlayer1: OnDamageListener
    private lateinit var onDamageListenerPlayer2: OnDamageListener

    private lateinit var onHealListenerPlayer1: OnHealListener
    private lateinit var onHealListenerPlayer2: OnHealListener

    private lateinit var onSpellUsePlayer: OnSpellUseListener
    //endregion

    //region timer
    private var seconds: Long = 0
    //endregion

    override fun settingsView() {
        super.settingsView()
        gameThread = LoopingThread()
        gameThread.run()
        skillContainer1 = view.findViewById(R.id.skill_container_player1)
        skillContainer2 = view.findViewById(R.id.skill_container_player2)
        a = activity as MainActivity
        soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)

        createListeners()

        if (CHEAT_ENABLE) {
            view.bar_container1.setOnClickListener { unit1.hp -= 200 }
            view.bar_container2.setOnClickListener { unit2.hp -= 200 }
        }
        slotsPlayer1 = arrayListOf(
                view.p1_slot1,
                view.p1_slot2,
                view.p1_slot3)

        slotsPlayer2 = arrayListOf(
                view.p2_slot1,
                view.p2_slot2,
                view.p2_slot3)

        val layoutTransition1 = view.effects_container_player1.layoutTransition
        layoutTransition1.enableTransitionType(LayoutTransition.CHANGING)
        val layoutTransition2 = view.effects_container_player2.layoutTransition
        layoutTransition2.enableTransitionType(LayoutTransition.CHANGING)

        view.pause.setOnClickListener {
            if (gameThread.isRunning) {
                onPause()
            } else {
                onResume()
            }
        }
    }

    fun setUnits(unitsPlayer1: ArrayList<Unit>, unitsPlayer2: ArrayList<Unit>) {
        this.data1 = unitsPlayer1
        this.data2 = unitsPlayer2
        //region sound
        soundPool.release()
        soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        unitsPlayer1.forEach {
            it.spells.forEach {
                if (it.commentResId != 0) {
                    it.commentId = soundPool.load(activity, it.commentResId, 1)
                }
                if (it.soundResId != 0) {
                    it.soundId = soundPool.load(activity, it.soundResId, 1)
                }
            }
        }
        unitsPlayer2.forEach {
            it.spells.forEach {
                if (it.commentResId != 0) {
                    it.commentId = soundPool.load(activity, it.commentResId, 1)
                }
                if (it.soundResId != 0) {
                    it.soundId = soundPool.load(activity, it.soundResId, 1)
                }
            }
        }
        //endregion
    }

    private fun setTarget(unit: Unit, target: Unit) {
        unit.target = target
        unit.focus = target
    }

    private fun nextUnitPlayer1() {
        unit1 = unitsPlayer1[0]

        skillContainer1.removeAllViewsInLayout()

        addAllSpells(unit1, skillContainer1)

        view.bar_container1.removeAllViewsInLayout()

        addAllBars(unit1, view.bar_container1)

        setTarget(unit1, unit2)
        setTarget(unit2, unit1)

        controller1.unit = unit1
        controller1.begin()

        view.effects_container_player1.removeAllViewsInLayout()

        unit1.onMoveEffectsListeners.add(onMoveEffectsListenerPlayer1)

        unit1.onAliveChangeListeners.add(onAliveListenerPlayer1)

        unit1.onDamageListeners.add(onDamageListenerPlayer1)

        unit1.onHealListeners.add(onHealListenerPlayer1)

        unit1.onSpellUseListeners.add(onSpellUsePlayer)

        slotsPlayer1.forEach { it.removeAllViews() }
        imagesPlayer1.forEach { slotsPlayer1[imagesPlayer1.indexOf(it)].addView(it) }
    }

    private fun nextUnitPlayer2() {
        unit2 = unitsPlayer2[0]

        addAllSpells(unit2, skillContainer2)

        view.bar_container2.removeAllViewsInLayout()

        addAllBars(unit2, view.bar_container2)

        setTarget(unit1, unit2)
        setTarget(unit2, unit1)

        controller2.unit = unit2
        controller2.begin()

        view.effects_container_player2.removeAllViewsInLayout()

        unit2.onMoveEffectsListeners.add(onMoveEffectsListenerPlayer2)

        unit2.onAliveChangeListeners.add(onAliveListenerPlayer2)

        unit2.onDamageListeners.add(onDamageListenerPlayer2)

        unit2.onHealListeners.add(onHealListenerPlayer2)

        unit2.onSpellUseListeners.add(onSpellUsePlayer)

        slotsPlayer2.forEach { it.removeAllViews() }
        imagesPlayer2.forEach { slotsPlayer2[imagesPlayer2.indexOf(it)].addView(it) }
    }

    private fun bindAll() {
        //region bind

        unitsPlayer1 = arrayListOf()
        unitsPlayer2 = arrayListOf()

        unitsPlayer1.addAll(data1)
        unitsPlayer2.addAll(data2)

        unit1 = unitsPlayer1[0]
        unit2 = unitsPlayer2[0]

        unitsPlayer1.forEach {
            it.bindBars(activity, Side.LEFT)
            it.bindSpells(activity)
            it.release()
        }
        unitsPlayer2.forEach {
            it.bindBars(activity, Side.RIGHT)
            it.bindSpells(activity)
            it.release()
        }
        setTarget(unit1, unit2)
        setTarget(unit2, unit1)
        //endregion

        //region units
        units = arrayListOf()
        units.addAll(unitsPlayer1)
        units.addAll(unitsPlayer2)

        statCollectorPlayer1 = StatCollector(unitsPlayer1)
        statCollectorPlayer2 = StatCollector(unitsPlayer2)
        //endregion

        //region game thread
        units.forEach { gameThread.onTickEventListeners.add(it) }
        gameThread.onTickEventListeners.add(controller1)
        gameThread.onTickEventListeners.add(controller2)

        gameThread.onTickEventListeners.add(statCollectorPlayer1)
        gameThread.onTickEventListeners.add(statCollectorPlayer2)

        //endregion

        //region game cycle
        controller1.unit = unit1
        controller2.unit = unit2

        gameCycleListeners.add(controller1)
        gameCycleListeners.add(controller2)

        gameCycleListeners.addAll(unitsPlayer1)
        gameCycleListeners.addAll(unitsPlayer2)
        //endregion
    }

    private fun createListeners() {
        onMoveEffectsListenerPlayer1 = object : OnMoveEffectsListener {
            override fun onPut(effect: Effect) {
                this@GameScreen.view.effects_container_player1.addView(effect.effectView.view)
            }

            override fun onRemove(effect: Effect) {
                this@GameScreen.view.effects_container_player1.removeView(effect.effectView.view)
            }
        }
        onMoveEffectsListenerPlayer2 = object : OnMoveEffectsListener {
            override fun onPut(effect: Effect) {
                this@GameScreen.view.effects_container_player2.addView(effect.effectView.view)
            }

            override fun onRemove(effect: Effect) {
                this@GameScreen.view.effects_container_player2.removeView(effect.effectView.view)
            }
        }
        onAliveListenerPlayer1 = OnChangeListener {
            unit1.onMoveEffectsListeners.remove(onMoveEffectsListenerPlayer1)
            unit1.onAliveChangeListeners.remove(onAliveListenerPlayer1)
            unit1.onDamageListeners.remove(onDamageListenerPlayer1)
            unit1.onHealListeners.remove(onHealListenerPlayer1)
            unit1.onSpellUseListeners.remove(onSpellUsePlayer)
            unitsPlayer1.remove(unit1)
            if (unitsPlayer1.size == 0) {
                gameEnd()
                if (!a.gameDialog.isShowing) {
                    a.gameDialog.idTextHeader = R.string.player2
                    statCollectorPlayer2.isVictory = true
                    a.gameDialog.create(super.parent)
                    view.hud.invalidate()
                }
            } else {
                imagesPlayer1.removeAt(0)
                nextUnitPlayer1()
            }
        }
        onAliveListenerPlayer2 = OnChangeListener {
            unit2.onMoveEffectsListeners.remove(onMoveEffectsListenerPlayer2)
            unit2.onAliveChangeListeners.remove(onAliveListenerPlayer2)
            unit2.onDamageListeners.remove(onDamageListenerPlayer2)
            unit2.onHealListeners.remove(onHealListenerPlayer2)
            unit2.onSpellUseListeners.remove(onSpellUsePlayer)
            unitsPlayer2.remove(unit2)
            if (unitsPlayer2.size == 0) {
                gameEnd()
                if (!a.gameDialog.isShowing) {
                    a.gameDialog.idTextHeader = R.string.player1
                    statCollectorPlayer1.isVictory = true
                    a.gameDialog.create(super.parent)
                    view.hud.invalidate()
                }
            } else {
                imagesPlayer2.removeAt(0)
                nextUnitPlayer2()
            }
        }
        onDamageListenerPlayer1 = object : OnDamageListener {

            override fun makeDamage(damage: Double, spell: SpellDamage) {
                view.hud.drawPlayer1Damage(damage, (spell as Spell).view)
            }

            override fun makeDamage(damage: Double, effect: Effect) {
                view.hud.drawPlayer1Damage(damage, effect.effectView)
            }
        }
        onHealListenerPlayer1 = object : OnHealListener {
            override fun makeHeal(heal: Double) {
            }

            override fun makeHeal(heal: Double, spell: SpellHeal) {
                view.hud.drawPlayer1Damage(-heal, (spell as Spell).view)
            }

            override fun makeHeal(heal: Double, effect: Effect) {
                view.hud.drawPlayer1Damage(-heal, effect.effectView)
            }
        }
        onDamageListenerPlayer2 = object : OnDamageListener {
            override fun makeDamage(damage: Double, spell: SpellDamage) {
                view.hud.drawPlayer2Damage(damage, (spell as Spell).view)
            }

            override fun makeDamage(damage: Double, effect: Effect) {
                view.hud.drawPlayer2Damage(damage, effect.effectView)
            }
        }
        onHealListenerPlayer2 = object : OnHealListener {
            override fun makeHeal(heal: Double) {

            }

            override fun makeHeal(heal: Double, spell: SpellHeal) {
                view.hud.drawPlayer2Damage(-heal, (spell as Spell).view)
            }

            override fun makeHeal(heal: Double, effect: Effect) {
                view.hud.drawPlayer2Damage(-heal, effect.effectView)
            }
        }
        onSpellUsePlayer = OnSpellUseListener {
            if (it.commentId != 0) {
                soundPool.play(it.commentId, 1f, 1f, 2, 0, 1f)
            }
            if (it.soundId != 0) {
                soundPool.play(it.soundId, 1f, 1f, 2, 0, 1f)
            }
        }
    }

    private fun addAnimation(unit: Unit, frameView: ImageView) {
        val startFrameIndex = 0
        var frameIndex = 0
        var rest = 0
        var tempFrameIndex = frameIndex
        unit.onSpellUseListeners.add(OnSpellUseListener {
            frameIndex = unit.spells.indexOf(it)
            if (frameIndex + 1 < unit.framesId.size) {
                frameView.setImageResource(unit.framesId[frameIndex + 1])
            }
        })
        unit.onTickListeners.add(OnTickListener {
            if (tempFrameIndex == frameIndex) {
                rest++
                if (rest % 5 == 0) {
                    frameView.setImageResource(unit.framesId[startFrameIndex])
                }
            } else {
                tempFrameIndex = frameIndex
                rest = 0
            }
        })
    }

    override fun releaseDate() {
        super.releaseDate()
        val fons = arrayOf(R.drawable.fon_battle1, R.drawable.fon_battle2,
                R.drawable.fon_battle3)

        view.setBackgroundResource(fons[Random().nextInt(fons.size)])

        bindAll()
        gameBegin()
        var state = 0
        gameThread.speed = speed
        //region timer
        gameThread.onTickEventListeners.add(OnTickListener {
            state += 1
            view.hud.tick()
            if (state % 10 == 0) {
                seconds += 1000
                val date = Date()
                date.time = seconds
                view.timer.text = DateFormat.format("mm:ss", date)
            }
        })
        //endregion

        //region add listeners
        unit1.onMoveEffectsListeners.add(onMoveEffectsListenerPlayer1)
        unit2.onMoveEffectsListeners.add(onMoveEffectsListenerPlayer2)

        unit1.onAliveChangeListeners.add(onAliveListenerPlayer1)
        unit2.onAliveChangeListeners.add(onAliveListenerPlayer2)

        unit1.onDamageListeners.add(onDamageListenerPlayer1)
        unit2.onDamageListeners.add(onDamageListenerPlayer2)

        unit1.onHealListeners.add(onHealListenerPlayer1)
        unit2.onHealListeners.add(onHealListenerPlayer2)

        unit1.onSpellUseListeners.add(onSpellUsePlayer)
        unit2.onSpellUseListeners.add(onSpellUsePlayer)
        //endregion

        //region view
        skillContainer1.removeAllViewsInLayout()
        skillContainer2.removeAllViewsInLayout()

        if (controller2 is AIController) {
            view.spells.weightSum = 50f
        }

        addAllSpells(unit1, skillContainer1)
        addAllSpells(unit2, skillContainer2)

        view.bar_container1.removeAllViewsInLayout()
        view.bar_container2.removeAllViewsInLayout()

        addAllBars(unit1, view.bar_container1)
        addAllBars(unit2, view.bar_container2)

        view.effects_container_player1.removeAllViewsInLayout()
        view.effects_container_player2.removeAllViewsInLayout()

        //region animation
        imagesPlayer1 = arrayListOf()
        imagesPlayer2 = arrayListOf()

        slotsPlayer1.forEach { it.removeAllViews() }
        slotsPlayer2.forEach { it.removeAllViews() }

        slotsPlayer1.forEach {
            val image = ImageView(activity)
            imagesPlayer1.add(image)
            it.addView(image)
        }
        slotsPlayer2.forEach {
            val image = ImageView(activity)
            imagesPlayer2.add(image)
            it.addView(image)
        }

        for (i in 0 until unitsPlayer1.size) {
            addAnimation(unitsPlayer1[i], slotsPlayer1[i].getChildAt(0) as ImageView)
        }
        for (i in 0 until unitsPlayer2.size) {
            addAnimation(unitsPlayer2[i], slotsPlayer2[i].getChildAt(0) as ImageView)
        }

        imagesPlayer2.forEach { it.scaleX = -1F }
        //endregion

        view.pause.setBackgroundResource(R.drawable.icon_pause)
    }

    private fun addAllBars(unit: Unit, barContainer: LinearLayout) {
        for (i in 0 until unit.bars.size) {
            barContainer.addView(unit.bars[i].root)
        }
    }

    private fun addAllSpells(unit: Unit, skillContainer: LinearLayout) {
        for (i in 0 until unit.spells.size) {
            (unit.spells[i].view.parent as ViewGroup?)?.removeView(unit.spells[i].view)
            skillContainer.addView(unit.spells[i].view)
        }
    }

    fun gameBegin() {
        for (i in 0 until gameCycleListeners.size) {
            gameCycleListeners[i].begin()
        }
        onResume()
    }

    fun gameEnd() {
        for (i in 0 until gameCycleListeners.size) {
            gameCycleListeners[i].end()
        }
        gameCycleListeners.clear()
        gameThread.finish()
        seconds = 0
    }

    fun onPause() {
        gameThread.onStop()
        view.pause.setBackgroundResource(R.drawable.icon_play)
        gameCycleListeners.forEach { it.pause() }
    }

    fun onResume() {
        gameThread.onStart()
        view.pause.setBackgroundResource(R.drawable.icon_pause)
        gameCycleListeners.forEach { it.resume() }
    }
}
