package com.blaster.platform

import com.blaster.business.ParseInteractor
import com.blaster.business.PrintInteractor
import javax.inject.Inject

/*
 * This is a main class for this app
 */
class LemApp {
    @Inject
    lateinit var parseInteractor: ParseInteractor

    @Inject
    lateinit var printInteractor: PrintInteractor

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
        val path = "com.blaster.platform.LemAppKt::main"
        val parsed = parseInteractor.parseDef(path)

        /*
        And a last thing..
         */
        printInteractor.printArticle(path, parsed)
    }
}

/*
Our main global foo
 */
fun main() {
    /*
    We start by creating our main class
    It is multiline, of course
    */
    // include decl com.blaster.platform.LemAppKt::main
    val lemApp = LemApp()

    // We will include another method here because why not?
    // include def com.blaster.platform.LemApp::render

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