package com.blaster.platform

import com.blaster.business.ParseInteractor
import com.blaster.business.PrintInteractor
import javax.inject.Inject

/*
 * This is a main class for this app
 */
class LemApp {
    /*
    This is a first property
     */
    @Inject
    lateinit var parseInteractor: ParseInteractor

    /*
    This is a second property
     */
    @Inject
    lateinit var printInteractor: PrintInteractor

    init {
        LEM_COMPONENT.inject(this)
    }

    /*
    And this is a main call for LemApp
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
This is a global declaration
 */
fun main() {
    // include decl com.blaster.platform.LemApp

    val lemApp = LemApp()
    lemApp.render()
}