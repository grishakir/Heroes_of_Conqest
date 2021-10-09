package com.thekingames.medivalwarriors2.core.view.activity

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.Time
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.controller.PlayerController
import com.thekingames.medivalwarriors2.core.controller.ai.AIController
import com.thekingames.medivalwarriors2.core.controller.ai.SimpleAIController
import com.thekingames.medivalwarriors2.core.model.interfaces.OnChangeListener
import com.thekingames.medivalwarriors2.core.model.player.Player
import com.thekingames.medivalwarriors2.core.view.activity.Settings.CHEAT_ENABLE
import com.thekingames.medivalwarriors2.core.view.screens.*
import com.thekingames.medivalwarriors2.core.view.screens.dialog.ChooseItemDialog
import com.thekingames.medivalwarriors2.core.view.screens.dialog.GameDialog
import com.thekingames.medivalwarriors2.core.view.screens.dialog.MenuDialog
import com.thekingames.medivalwarriors2.core.view.screens.dialog.MessageDialog
import com.thekingames.screenmanager.Screen
import kotlinx.android.synthetic.main.root.*
import java.io.*
import java.util.*

const val MENU = 0
const val TASKS = 1
const val HEROES = 2
const val INVENTORY = 3
const val SHOP = 4
const val LIBRARY = 5
const val PLAYER_STATISTIC = 6
const val HIRE_HERO = 7

class MainActivity : AppCompatActivity() {
    lateinit var parent: ViewGroup
    lateinit var menuScreen: MenuScreen
    lateinit var chooseChapterScreen: ChooseChapterScreen
    lateinit var shop1Screen: Shop1Screen
    lateinit var inventoryScreen: InventoryScreen
    lateinit var heroesScreen: ShopHeroesScreen
    lateinit var chooseHeroScreen: ChooseHeroScreen
    lateinit var libraryScreen: LibraryScreen
    lateinit var statScreen: StatScreen
    lateinit var chapter1Screen: Chapter1Screen
    lateinit var hireHeroScreen: HireHeroScreen
    lateinit var tasksScreen: TasksScreen
    lateinit var playerStatisticScreen: PlayerStatisticScreen
    var bottomBar: ArrayList<Screen> = arrayListOf()
    var gameScreen: GameScreen? = null
    val screenHistory: ArrayList<Screen> = arrayListOf()
    private lateinit var currentScreen: Screen

    fun setScreen(value: Screen) {
        currentScreen = value
        if (currentScreen == menuScreen) {
            screenHistory.clear()
            showBar()
        }
        if (!screenHistory.contains(value)) {
            screenHistory.add(currentScreen)
        }
        currentScreen.show()
    }

    lateinit var gamePvP: GameScreen
    lateinit var gamePvAI: GameScreen
    lateinit var gameCompany: GameScreen
    lateinit var gameAIvsAI: GameScreen

    private var onBackPressedListeners = arrayListOf<OnChangeListener>()

    var gameDialog: GameDialog = GameDialog(this)
    var menuDialog: MenuDialog = MenuDialog(this)
    var chooseItemDialog: ChooseItemDialog = ChooseItemDialog(this)
    var messageDialog: MessageDialog = MessageDialog(this)

    lateinit var isNewIcons: Array<ImageView>
    lateinit var isNewValues: Array<Boolean>

    private val fileName = "player"
    lateinit var player: Player
    lateinit var outputStream: FileOutputStream
    lateinit var inputStream: FileInputStream
    lateinit var objectInputStream: ObjectInputStream
    lateinit var objectOutputStream: ObjectOutputStream

    lateinit var soundPool: SoundPool
    private var soundClick: Int = 0
    lateinit var media: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root)
        parent = findViewById(R.id.parent)
        music_switcher.setOnClickListener {
            if (media.isPlaying) {
                music_switcher.setImageResource(R.drawable.icon_music_off)
                media.pause()
            } else {
                music_switcher.setImageResource(R.drawable.icon_music_on)
                media.start()
            }
        }
        loadHero()
        soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)
        media = MediaPlayer.create(this, R.raw.castlecall)
        media.setVolume(0.3f, 0.3f)
        media.isLooping = true
        soundClick = soundPool.load(this, R.raw.click1, 5)
        menuScreen = MenuScreen(parent)
        shop1Screen = Shop1Screen(parent)
        inventoryScreen = InventoryScreen(parent)
        heroesScreen = ShopHeroesScreen(parent)
        libraryScreen = LibraryScreen(parent)
        chooseHeroScreen = ChooseHeroScreen(parent)
        chapter1Screen = Chapter1Screen(parent)
        hireHeroScreen = HireHeroScreen(parent)
        statScreen = StatScreen(parent)
        tasksScreen = TasksScreen(parent)
        playerStatisticScreen = PlayerStatisticScreen(parent)
        chooseChapterScreen = ChooseChapterScreen(parent)

        gamePvP = GameScreen(parent, PlayerController(), PlayerController())
        gamePvAI = GameScreen(parent, PlayerController(), AIController())
        gameCompany = GameScreen(parent, PlayerController(), SimpleAIController())
        gameAIvsAI = GameScreen(parent, AIController(), AIController())

        setScreen(menuScreen)

        isNewValues = arrayOf(false, player.tasksEnable.find { (it == true) && player.tasksEnable.indexOf(it) <= player.access } != null, player.getCurrentHero().points > 0, false, true, false, false, player.crystals >= hireHeroScreen.price)
        isNewIcons = arrayOf(n1, n2, n3, n4, n5, n6, n7, n8)

        player.onChangeCoinsListener = OnChangeListener {
            releaseBar()
        }
        player.onChangeCrystalsListener = OnChangeListener {
            releaseBar()
            if (player.crystals >= hireHeroScreen.price) {
                new(HIRE_HERO)
            }
        }

        player.onLvlChangeListener = OnChangeListener {
            new(HEROES);new(SHOP)
        }

        player.onChangeAccessListener = OnChangeListener {
            new(TASKS)
        }

        bottomBar.add(menuScreen)
        bottomBar.add(tasksScreen)
        bottomBar.add(heroesScreen)
        bottomBar.add(inventoryScreen)
        bottomBar.add(shop1Screen)
        bottomBar.add(libraryScreen)
        bottomBar.add(playerStatisticScreen)
        bottomBar.add(hireHeroScreen)


        if (CHEAT_ENABLE) {
            coins.setOnClickListener { player.coins += 500 }
            crystal.setOnClickListener { player.crystals += 100 }
        }

        activateBar(menuScreen)
        releaseBar()
        releaseNews()
        //region settings
        volumeControlStream = AudioManager.STREAM_MUSIC
        //endregion
    }

    private fun unActivateAllBottomBar() {
        for (i in 0 until bottom_bar.childCount) {
            bottom_bar.getChildAt(i).background = null
            bottom_bar.getChildAt(i).isEnabled = true
        }
    }

    private fun activateBar(screen: Screen) {
        unActivateAllBottomBar()
        val index = bottomBar.indexOf(screen)
        if (index != -1) {
            val v = bottom_bar.getChildAt(index)
            v.setBackgroundResource(R.drawable.fon_white_trans)
            v.isEnabled = false
        }
    }

    fun showBar() {
        bottom_bar_container.visibility = View.VISIBLE
        header_bar.visibility = View.VISIBLE
    }

    fun hideBar() {
        bottom_bar_container.visibility = View.GONE
        header_bar.visibility = View.GONE
    }

    fun releaseBar() {
        coins.text = player.coins.toString()
        crystal.text = player.crystals.toString()

    }

    fun new(i: Int) {
        isNewValues[i] = true
        releaseNews()
    }

    private fun releaseNews() {
        isNewIcons.forEach {
            val i = isNewIcons.indexOf(it)
            it.visibility = if (isNewValues[i]) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }

    fun setHeader(textId: Int) {
        text_header.setText(textId)
    }

    fun onClickMenu(v: View) {
        soundPool.play(soundClick, 0.8f, 0.8f, 5, 0, 1.0f)
        val menuButtons = (menuScreen).contentMenu
        when (v) {
            menuButtons.getChildAt(0) -> {
                gameScreen = gamePvP
                setScreen(chooseHeroScreen)
                hideBar()
            }
            menuButtons.getChildAt(1) -> {
                gameScreen = gameCompany
                setScreen(chooseChapterScreen)
                hideBar()
            }
            menuButtons.getChildAt(2) -> {
                gameScreen = gamePvAI
                setScreen(chooseHeroScreen)
                hideBar()
            }
            menuButtons.getChildAt(3) -> {
                gameScreen = gameAIvsAI
                gameScreen?.speed = 100
                setScreen(chooseHeroScreen)
                hideBar()
            }
            bottom_bar.getChildAt(MENU) -> {
                screenHistory.clear()
                setScreen(bottomBar[bottom_bar.indexOfChild(v)])
                activateBar(bottomBar[bottom_bar.indexOfChild(v)])
                isNewValues[bottom_bar.indexOfChild(v)] = false
                releaseNews()
            }

            bottom_bar.getChildAt(TASKS) -> {
                setScreen(bottomBar[bottom_bar.indexOfChild(v)])
                activateBar(bottomBar[bottom_bar.indexOfChild(v)])
                isNewValues[bottom_bar.indexOfChild(v)] = false
                releaseNews()
            }

            bottom_bar.getChildAt(HEROES) -> {
                setScreen(bottomBar[bottom_bar.indexOfChild(v)])
                activateBar(bottomBar[bottom_bar.indexOfChild(v)])
                isNewValues[bottom_bar.indexOfChild(v)] = false
                releaseNews()
            }

            bottom_bar.getChildAt(INVENTORY) -> {
                setScreen(bottomBar[bottom_bar.indexOfChild(v)])
                activateBar(bottomBar[bottom_bar.indexOfChild(v)])
                isNewValues[bottom_bar.indexOfChild(v)] = false
                releaseNews()
            }
            bottom_bar.getChildAt(SHOP) -> {
                setScreen(bottomBar[bottom_bar.indexOfChild(v)])
                activateBar(bottomBar[bottom_bar.indexOfChild(v)])
                isNewValues[bottom_bar.indexOfChild(v)] = false
                releaseNews()
            }
            bottom_bar.getChildAt(LIBRARY) -> {
                setScreen(bottomBar[bottom_bar.indexOfChild(v)])
                activateBar(bottomBar[bottom_bar.indexOfChild(v)])
            }
            bottom_bar.getChildAt(PLAYER_STATISTIC) -> {
                setScreen(bottomBar[bottom_bar.indexOfChild(v)])
                activateBar(bottomBar[bottom_bar.indexOfChild(v)])
                isNewValues[bottom_bar.indexOfChild(v)] = false
                releaseNews()
            }

            bottom_bar.getChildAt(HIRE_HERO) -> {
                setScreen(bottomBar[bottom_bar.indexOfChild(v)])
                activateBar(bottomBar[bottom_bar.indexOfChild(v)])
                isNewValues[bottom_bar.indexOfChild(v)] = false
                releaseNews()
            }
        }
        if (chooseItemDialog.isShowing) {
            chooseItemDialog.hide()
        }
    }

    override fun onPause() {
        super.onPause()
        if (gameScreen == currentScreen) {
            (gameScreen)?.onPause()
        }
        if (media.isPlaying) {
            music_switcher.setImageResource(R.drawable.icon_music_off)
            media.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (gameScreen == currentScreen) {
            (gameScreen)?.onResume()
        }
        if (!media.isPlaying) {
            music_switcher.setImageResource(R.drawable.icon_music_on)
            media.start()
        }
    }

    private fun saveHero() {
        try {
            outputStream = this.openFileOutput(fileName, Context.MODE_PRIVATE)
            objectOutputStream = ObjectOutputStream(outputStream)
            objectOutputStream.writeObject(player)
            objectOutputStream.close()
        } catch (w: IOException) {
            w.printStackTrace()
        }

    }

    private fun loadHero() {
        try {
            inputStream = this.openFileInput(fileName)
            objectInputStream = ObjectInputStream(inputStream)
            player = objectInputStream.readObject() as Player
            player.load(this)
            objectInputStream.close()
        } catch (e: IOException) {
            player = Player()
            player.load(this)
            Log.i("ERROR", e.message)
            saveHero()
        } catch (e: ClassNotFoundException) {
            player = Player()
            player.load(this)
            saveHero()
        }

    }

    override fun onStop() {
        super.onStop()
        val time = Time()
        time.setToNow()
        if (player.savedDay != time.yearDay) {
            player.tasksEnable = Array(10) { true }
            player.savedDay = time.yearDay
        }
        saveHero()
        Log.i("data", "Has been saved player data")
    }

    override fun onBackPressed() {
        onBackPressedListeners.forEach { it.change() }
        if (!gameDialog.isShowing
                && !menuDialog.isShowing
                && !chooseItemDialog.isShowing) {
            when (currentScreen) {
                menuScreen -> {
                    finish()
                }

                gameScreen -> {
                    menuDialog.gameScreen = currentScreen as GameScreen
                    menuDialog.create(gameScreen?.parent)
                }
                else -> {
                    screenHistory.removeAt(screenHistory.size - 1)
                    setScreen(screenHistory[screenHistory.size - 1])
                    activateBar(currentScreen)
                }
            }
        }
    }
}
