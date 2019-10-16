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

fun main(args: Array<String>) {
    val lemApp = LemApp()
    if (args.isEmpty()) {
        println("No args, falling back to the defaults!")
        return lemApp.render(File("src/main/kotlin"), File("scenarios/scenario_lem"), File("articles/scenario_lem.html"))
    }
    check(args.size == 3) { "Wrong number of parameters!" }
    val sourceRoot = File(args[0])
    check(sourceRoot.exists()) { "Sources rout doesn't exists!" }
    val scenarioFile = File(args[1])
    check(scenarioFile.exists()) { "Scenario file doest't exists!" }
    val output = File(args[2])
    check(output.parentFile.exists()) { "Output folder doesn't exists!" }
    lemApp.render(sourceRoot, scenarioFile, output)
}