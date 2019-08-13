package com.blaster.platform

import com.blaster.business.InteractorParse
import com.blaster.business.InteractorPrint
import javax.inject.Inject

class LemApp {

    // omit
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
    // header h1 Lem app article

    // header h2 We will start by creating a main class for our app.


    // include link Kotlin grammar repo; This repo contains some of the grammars used throughout the article;https://github.com/antlr/grammars-v4/tree/master/kotlin
    // include picture This is a cool picture;This picture looks cool;https://ichef.bbci.co.uk/news/660/media/images/83351000/jpg/_83351971_explorer164gowerpicturebygarethjames.jpg


    val lemApp = LemApp()
    lemApp.render()

    // header h2 Now we can go to includes.

    // include decl com.blaster.platform.LemApp
}