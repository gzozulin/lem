package com.blaster.platform

import com.blaster.business.ParseMethodUseCase
import com.blaster.business.PrintUseCase
import java.io.File
import javax.inject.Inject

/*
 * This is a main class for this app
 */
class LemApp {
    @Inject
    lateinit var parseMethodUseCase: ParseMethodUseCase

    @Inject
    lateinit var printUseCase: PrintUseCase

    init {
        LEM_COMPONENT.inject(this)
    }

    /*
     * And this is a main call for LemApp
     */
    fun render() {
        parseMethodUseCase.parseMethod("src/main/kotlin/com/blaster/platform/LemApp.kt:-:main")
        printUseCase.printArticles()
    }
}

fun main() {
    /*
    We start by creating our main class
    It is multiline, of course
    */
    val lemApp = LemApp()

    // and then we simply run render() on it

    // and a second comment in a row
    // include decl com.blaster.platform.LemModule
    // include decl com.blaster.platform.LemModule::provideFreemarkerConfig
    // and a third one
    lemApp.render()

    /*
    Then we do a lot of random garbage code just to showcase the approach.
    The group with index 0 is always the entire matched String. Indices greater than 0, instead, represent groups in the regular expression, delimited by parentheses, such as ([bc]+) in our example.
    We can also destructure MatchResult instances in an assignment statement:
     */

    println("Done!")

    val runnable = Runnable {
        println("Runnable is good") // runnable is good!
    }

    Thread(runnable).start()

    /*
    And here we will put a comment as well
    */
}