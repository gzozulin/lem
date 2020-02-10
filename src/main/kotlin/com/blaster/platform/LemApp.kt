package com.blaster.platform

import com.blaster.business.InteractorParse
import com.blaster.business.InteractorPrint
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.File
import java.net.URL
import javax.inject.Inject

val kodein = Kodein {
    bind<InteractorParse>() with singleton { InteractorParse() }
}

class LemApp {

    private val interactorParse: InteractorParse by kodein.instance()

    @Inject
    lateinit var interactorPrint: InteractorPrint

    init {
        LEM_COMPONENT.inject(this)
    }

    private fun render(sourceUrl: URL, sourceRoot: File, scenarioFile: File, outputfile: File) {
        val parsed = interactorParse.parseScenario(sourceUrl, sourceRoot, scenarioFile)
        interactorPrint.printArticle(outputfile, parsed)
    }

    fun renderScenarios(url: String, sources: String, scenarios: String, output: String) {
        val scenariosDir = File(scenarios)
        val outputDir = File(output)
        scenariosDir.list()!!.forEach { filename ->
            val sourcesUrl = URL("https", "github.com", url)
            val sourcesRoot = File(sources)
            val scenarioFile = File(scenariosDir, filename)
            val outputFile = File(outputDir, "$filename.html")
            render(sourcesUrl, sourcesRoot, scenarioFile, outputFile)
        }
    }
}

// This is an application main entry point. Here we define all the projects we want to process into the articles.
fun main() {
    val lemApp = LemApp()
    lemApp.renderScenarios("/madeinsoviets/lem/blob/develop/",      "src/main/kotlin",              "scenarios",            "articles")
    lemApp.renderScenarios("/madeinsoviets/blaster/blob/develop/",  "../blaster/src/main/kotlin",   "../blaster/scenarios", "../blaster/articles")
}