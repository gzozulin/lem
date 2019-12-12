package com.blaster.platform

import com.blaster.business.InteractorParse
import com.blaster.business.InteractorPrint
import java.io.File
import java.lang.IllegalArgumentException
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class LemApp {
    @Inject
    lateinit var interactorParse: InteractorParse

    @Inject
    lateinit var interactorPrint: InteractorPrint

    init {
        LEM_COMPONENT.inject(this)
    }

    fun render(sourceUrl: String, sourceRoot: File, scenarioFile: File, output: File) {
        val parsed = interactorParse.parseScenario(sourceUrl, sourceRoot, scenarioFile)
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
            lemApp.render("https://github.com/madeinsoviets/lem/blob/develop/", File("src/main/kotlin"), File("scenarios", it), File("articles", "$it.html"))
        }
        return
    }
    // After that we will extract the necessary settings one by one while checking the actual values in the process
    check(args.size == 3) { "Wrong number of parameters!" }
    val sourceRoot = File(args[0])
    check(sourceRoot.exists()) { "Sources rout doesn't exists!" }
    val sourceUrl = args[1]
    try {
        URL(sourceUrl)
    } catch (e: MalformedURLException) {
        throw IllegalArgumentException(e)
    }
    val scenarioFile = File(args[2])
    check(scenarioFile.exists()) { "Scenario file doest't exists!" }
    val output = File(args[3])
    check(output.parentFile.exists()) { "Output folder doesn't exists!" }
    // When everything is extracted - we can proceed to rendering of the task
    lemApp.render(sourceUrl, sourceRoot, scenarioFile, output)
}