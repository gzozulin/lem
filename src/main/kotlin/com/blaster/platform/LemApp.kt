package com.blaster.platform

import com.blaster.business.ParseUseCase
import com.blaster.business.PrintUseCase
import java.io.File
import javax.inject.Inject

class LemApp {
    @Inject
    lateinit var parseUseCase: ParseUseCase

    @Inject
    lateinit var printUseCase: PrintUseCase

    init {
        LEM_COMPONENT.inject(this)
    }

    fun render() {
        parseUseCase.parseRoots(listOf(File("src/main/kotlin/com/blaster/platform/LemApp.kt")))
        printUseCase.printArticles()
    }
}

fun main() {
    /*
     * We start by creating our main class
     * It is multiline, of course
     */
    val lemApp = LemApp()

    // and then we simply run render() on it
    // and a second comment in a row
    // and a third one
    lemApp.render()

    println("Done!")
}