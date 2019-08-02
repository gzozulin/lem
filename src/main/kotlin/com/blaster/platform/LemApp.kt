package com.blaster.platform

import com.blaster.business.ParseDefUseCase
import com.blaster.business.PrintUseCase
import javax.inject.Inject

/*
 * This is a main class for this app
 */
class LemApp {
    @Inject
    lateinit var parseDefUseCase: ParseDefUseCase

    @Inject
    lateinit var printUseCase: PrintUseCase

    init {
        LEM_COMPONENT.inject(this)
    }

    /*
     * And this is a main call for LemApp
     */
    fun render() {
        /*
        This method also will be randomly commented
         */
        val path = "src/main/kotlin/com/blaster/platform/LemApp.kt:-:main"
        val parsed = parseDefUseCase.parseDef(path)

        /*
        And a last thing..
         */
        printUseCase.printArticle(path, parsed)
    }
}

fun main() {
    /*
    We start by creating our main class
    It is multiline, of course
    */
    val lemApp = LemApp()

    // We will include another method here because why not?
    // include def src/main/kotlin/com/blaster/platform/LemApp.kt:LemApp:render

    // and then we simply run render() on it
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