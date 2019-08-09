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

/*
The super main function
 */
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
    // include decl com.blaster.platform.LemAppKt::main

    /*
    You can use if, elseif and else directives to conditionally skip a section of the template.
    The condition-s must evaluate to a boolean value, or else an error will abort template processing.
    The elseif-s and else-s must occur inside if (that is, between the if start-tag and end-tag).
    The if can contain any number of elseif-s (including 0) and at the end optionally one else.
    Examples: if with 0 elseif and no else:
     */
}