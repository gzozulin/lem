package com.blaster.platform

import com.blaster.business.InteractorParse
import com.blaster.business.InteractorPrint
import javax.inject.Inject

/*
 * This is a main class for this app
 */
class LemApp {
    /*
    This is a first property
     */
    @Inject
    lateinit var interactorParse: InteractorParse

    /*
    This is a second property
     */
    @Inject
    lateinit var interactorPrint: InteractorPrint

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
        val parsed = interactorParse.parseDef(path)

        /*
        And a last thing..
         */
        interactorPrint.printArticle(path, parsed)
    }
}

/*
This is a global declaration
 */
fun main() {
    // include decl com.blaster.platform.LemAppKt::main
    val lemApp = LemApp()
    lemApp.render()
}