package com.blaster.platform

import com.blaster.business.InteractorParse
import com.blaster.business.InteractorPrint
import javax.inject.Inject

class LemApp {
    /*
    First prop
     */
    @Inject
    lateinit var interactorParse: InteractorParse

    /*
    Sec prop
     */
    @Inject
    lateinit var interactorPrint: InteractorPrint

    init {
        LEM_COMPONENT.inject(this)
    }

    /*
    Main foo
     */
    fun render() {
        val path = "com.blaster.platform.LemAppKt::main"
        val parsed = interactorParse.parseDef(path)
        interactorPrint.printArticle(path, parsed)
    }
}

fun main() {
    /*
    What about this type of comment?
    */
    val lemApp = LemApp()
    lemApp.render()

    // include decl com.blaster.platform.LemApp
}