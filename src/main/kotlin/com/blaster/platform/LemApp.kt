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

// This is an application main entry point. The only parameter is an array of command line arguments
fun main(args: Array<String>) {
    // We will create the application class first
    val lemApp = LemApp()
    // If we do not have any arguments - we will fall back to the default values
    if (args.isEmpty()) {
        println("No args, falling back to the defaults!")
        File("scenarios").list()!!.forEach {
            lemApp.render(File("src/main/kotlin"), File("scenarios", it), File("articles", "$it.html"))
        }
        return
    }
    // After that we will extract the necessary settings one by one while checking the actual values in the process
    check(args.size == 3) { "Wrong number of parameters!" }
    val sourceRoot = File(args[0])
    check(sourceRoot.exists()) { "Sources rout doesn't exists!" }
    val scenarioFile = File(args[1])
    check(scenarioFile.exists()) { "Scenario file doest't exists!" }
    val output = File(args[2])
    check(output.parentFile.exists()) { "Output folder doesn't exists!" }
    // When everything is extracted - we can proceed to rendering of the task
    lemApp.render(sourceRoot, scenarioFile, output)
}