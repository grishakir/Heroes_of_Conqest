package com.thekingames.company


open class TextChapter(var idName: Int, var idDescription: Int, var idTextArray: Int,var phrases: Array<Phrase>)
op
))

class TextChapter2 : TextChapter(R.string.name_chapter1, R.string.description_chapter1, R.array.chapter1,  arrayOf(
        Phrase(0,Speaker.AUTHOR),
        Phrase(1,Speaker.AUTHOR),
        Phrase(2,Speaker.HERO),
        Phrase(3,Speaker.AUTHOR),
        Phrase(4,Speaker.PLAYER),
        Phrase(5,Speaker.AUTHOR),
        Phrase(6,Speaker.HERO),
        Phrase(7,Speaker.AUTHOR)
))

class TextChapter3 : TextChapter(R.string.name_chapter2, R.string.description_chapter2, R.array.chapter2,  arrayOf(
        Phrase(0,Speaker.AUTHOR),
        Phrase(1,Speaker.AUTHOR),
        Phrase(2,Speaker.HERO),
        Phrase(3,Speaker.PLAYER),
        Phrase(4,Speaker.HERO),
        Phrase(5,Speaker.AUTHOR)
))

class TextChapter4 : TextChapter(R.string.name_chapter3, R.string.description_chapter3, R.array.chapter3,  arrayOf(
        Phrase(0,Speaker.AUTHOR),
        Phrase(1,Speaker.PLAYER),
        Phrase(2,Speaker.AUTHOR),
        Phrase(3,Speaker.PLAYER),
        Phrase(4,Speaker.AUTHOR),
        Phrase(5,Speaker.AI),
        Phrase(6,Speaker.AUTHOR),
        Phrase(7,Speaker.HERO),
        Phrase(8,Speaker.PLAYER),
        Phrase(9,Speaker.HERO),
        Phrase(10,Speaker.AUTHOR),
        Phrase(11,Speaker.AI),
        Phrase(12,Speaker.AUTHOR),
        Phrase(13,Speaker.HERO),
        Phrase(14,Speaker.PLAYER),
        Phrase(15,Speaker.AUTHOR)
))

enum class Speaker {
    AI, HERO, AUTHOR, PLAYER
}

data class Phrase(var index: Int, var speaker: Speaker)

