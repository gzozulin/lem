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

    fun render(sourceRoot: File, scenarioFile: File, output: File) {
        val parsed = interactorParse.parseScenario(sourceRoot, scenarioFile)
        interactorPrint.printArticle(output, parsed)
    }
}

fun main() {
    val lemApp = LemApp()
    lemApp.render(File("src/main/kotlin"), File("scenarios/scenario_lem"), File("articles/LemApp.html"))
}