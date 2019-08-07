package com.blaster.platform

import com.blaster.business.InteractorParse
import com.blaster.business.InteractorPrint
import javax.inject.Inject

class LemApp {
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

        /*
            Main foo is started like this
         */

        // Then like this
        /*
           And finally like this
         */


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

    val runnable = Runnable {
        val runnable = Runnable {
            val runnable = Runnable {
                val runnable = Runnable {
                    val runnable = Runnable {
                        /*
                        More complex stuff goes here
                         */
                        // And even more complex!!
                    }
                }
            }
        }
    }

    // include decl com.blaster.platform.LemApp
    // include def com.blaster.platform.LemApp::render
}