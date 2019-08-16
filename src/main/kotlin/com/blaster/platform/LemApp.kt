package com.blaster.platform

import com.blaster.business.InteractorParse
import com.blaster.business.InteractorPrint
import java.io.File
import javax.inject.Inject

class LemApp {
    @Inject
    lateinit var interactorParse: InteractorParse

    @Inject
    lateinit var interactorPrint: InteractorPrint

    init {
        LEM_COMPONENT.inject(this)
    }

    fun render(sources: List<Pair<File, String>>) {
        sources.forEach {
            val parsed = interactorParse.parseDef(it.second)
            interactorPrint.printArticle(it.second, parsed)
        }
    }
}

fun main() {
    val lemApp = LemApp()
    val sources = listOf(File("") to "com.blaster.platform.LemAppKt::main")
    lemApp.render(sources)
}