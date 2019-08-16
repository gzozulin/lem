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

    // header h2 Rule #1 for writing a good article: minimize your barrier to entry. Make it easy for your reader to be drawn in.

    /*A large opening paragraph at the start of an article is a huge barrier to entry. Your reader has to wade through a large wall of text before determining if the article is really interesting and worth reading. This requires a large expenditure of effort. Most people won’t bother.*/
    /*Keep your opening short and punchy. A one-sentence or two-sentence leading paragraph is an easy buy-in. You can skim it and read it in barely more time than it would take to scroll past.*/
    /*Start with something short and easy to engage with. Prove to your reader that you’re providing value, then ask them to expend effort.*/

    // header h2 We will start by creating a main class for our app.


    // include link Kotlin grammar repo; This repo contains some of the grammars used throughout the article;https://github.com/antlr/grammars-v4/tree/master/kotlin
    // include picture This is a cool picture;This picture looks cool;https://ichef.bbci.co.uk/news/660/media/images/83351000/jpg/_83351971_explorer164gowerpicturebygarethjames.jpg


    val lemApp = LemApp()
    lemApp.render()

    // header h2 Now we can go to includes.

    // inline decl com.blaster.platform.LemApp
}