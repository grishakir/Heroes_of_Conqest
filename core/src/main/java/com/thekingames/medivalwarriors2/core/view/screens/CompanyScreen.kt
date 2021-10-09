package com.thekingames.medivalwarriors2.core.view.screens

import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import com.thekingames.company.*
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.controller.ai.AIController
import com.thekingames.medivalwarriors2.core.controller.ai.SimpleAIController
import com.thekingames.medivalwarriors2.core.model.*
import com.thekingames.medivalwarriors2.core.model.Unit
import com.thekingames.medivalwarriors2.core.model.story.EnemyGenerator
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.screenmanager.Fragment
import com.thekingames.screenmanager.Screen
import kotlinx.android.synthetic.main.chapter1.view.*
import kotlinx.android.synthetic.main.choose_chapter.view.*
import kotlinx.android.synthetic.main.phrase_1.view.*

enum class DifficultyLevel {
    Easy, Hard
}

open class Chapter(var textChapter: TextChapter, var enemies: ArrayList<Unit>)

class Chapter1 : Chapter(TextChapter1(), arrayListOf(Rogue(), Hunter(), Rogue()))
class Chapter2 : Chapter(TextChapter2(), arrayListOf(Berserker(), Berserker(), Warrior()))
class Chapter3 : Chapter(TextChapter3(), arrayListOf(Rogue(), Rogue(), Hunter()))
class Chapter4 : Chapter(TextChapter4(), arrayListOf(Mage(), Mage(), Mage()))

class Chapter1Screen(parent: ViewGroup) : Screen(parent, R.layout.chapter1) {
    private val a = activity as MainActivity
    private lateinit var generator: EnemyGenerator
    lateinit var chapter: Chapter
    private lateinit var textArray: Array<String>
    private var lvl = 1
    private var dLevel: DifficultyLevel = DifficultyLevel.Easy

    override fun settingsView() {
        super.settingsView()
        generator = EnemyGenerator()
        view.next.setOnClickListener {
            val game = a.gameScreen as GameScreen
            val unit1 = a.player.getCurrentHero()
            if (dLevel == DifficultyLevel.Easy) {
                game.controller2 = SimpleAIController()
                chapter.enemies.forEach {
                    it.load(a)
                    if (chapter.enemies.indexOf(it) != chapter.enemies.size - 1) {
                        generator.settingsEasyUnit(lvl, it)
                    } else {
                        generator.settingsEasyBoss(lvl + 1, it)
                    }
                }
                game.setUnits(arrayListOf(unit1), chapter.enemies)
            } else if (dLevel == DifficultyLevel.Hard) {
                game.controller2 = AIController()
                chapter.enemies.forEach {
                    it.load(a)
                    if (chapter.enemies.indexOf(it) != chapter.enemies.size - 1) {
                        generator.settingsHardUnit(lvl, it)
                    } else {
                        generator.settingsHardBoss(lvl + 3, it)
                    }
                }
                game.setUnits(arrayListOf(unit1), chapter.enemies)
            }
            a.setScreen(game)

        }
        view.level_easy.setOnClickListener {
            view.level_hard.background = null
            it.setBackgroundResource(R.drawable.fon_trans)
            dLevel = DifficultyLevel.Easy
        }
        view.level_hard.setOnClickListener {
            view.level_easy.background = null
            it.setBackgroundResource(R.drawable.fon_trans)
            dLevel = DifficultyLevel.Hard
        }
    }

    override fun releaseDate() {
        super.releaseDate()
        view.phrases.removeAllViewsInLayout()
        (view as ScrollView).scrollTo(0, 0)
        lvl = a.player.getCurrentHero().lvl
        view.chapter_name.setText(chapter.textChapter.idDescription)
        textArray = a.resources.getStringArray(chapter.textChapter.idTextArray)
        val heroPhrasesArrayChapter1 = a.resources.getStringArray(R.array.hero_phrases_e1_ch1)
        val heroPhrasesArrayChapter2 = a.resources.getStringArray(R.array.hero_phrases_e1_ch2)
        chapter.textChapter.phrases.forEach {
            when (it.speaker) {
                Speaker.AI -> Phrase1(view.phrases, textArray[it.index], R.drawable.icon_ai1, false).show()
                Speaker.AUTHOR -> Phrase2(view.phrases, textArray[it.index], 0, true).show()
                Speaker.HERO -> {
                    val i = a.libraryScreen.heroes.indexOfFirst { it.javaClass == a.player.getCurrentHero().javaClass }
                    when {
                        it.index == 10 -> textArray[it.index] = heroPhrasesArrayChapter1[i]
                        it.index == 4 -> textArray[it.index] = heroPhrasesArrayChapter2[i]
                    }
                    Phrase1(view.phrases, textArray[it.index], a.libraryScreen.unitsIcons[i], false).show()
                }
                Speaker.PLAYER -> Phrase1(view.phrases, textArray[it.index], R.drawable.icon_player, false).show()
            }
        }

    }
}

open class Phrase(parent: ViewGroup, resId: Int, var text: String, var idSrc: Int, var isAuthor: Boolean) : Fragment(parent, resId) {
    override fun releaseDate() {
        super.releaseDate()
        view.text.text = text
        if (!isAuthor) {
            view.icon.setBackgroundResource(R.drawable.fon_icon1)
            view.icon.setImageResource(idSrc)
        }
    }
}

class Phrase1(parent: ViewGroup, text: String, idSrc: Int, isAuthor: Boolean) : Phrase(parent, R.layout.phrase_1, text, idSrc, isAuthor)

class Phrase2(parent: ViewGroup, text: String, idSrc: Int, isAuthor: Boolean) : Phrase(parent, R.layout.phrase_2, text, idSrc, isAuthor)

class ChooseChapterScreen(parent: ViewGroup) : Screen(parent, R.layout.choose_chapter) {
    private lateinit var chapters: ArrayList<Chapter>
    private val a = activity as MainActivity
    var chapterIndex = 0

    override fun settingsView() {
        super.settingsView()
        chapters = arrayListOf(Chapter1(), Chapter2(), Chapter3(), Chapter4())
        for (i in 0 until view.content.childCount) {
            (view.content.getChildAt(i) as TextView).setText(chapters[i].textChapter.idName)
            view.content.getChildAt(i).setOnClickListener {
                a.chapter1Screen.chapter = chapters[i]
                chapterIndex = i
                a.setScreen(a.chapter1Screen)
            }
        }

    }

    override fun releaseDate() {
        super.releaseDate()
        Array(chapters.size) { view.content.getChildAt(it) }.forEachIndexed { i, view -> view.isEnabled = i <= a.player.access }
    }
}