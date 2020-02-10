package com.blaster.platform

import com.blaster.business.*
import com.blaster.data.managers.kotlin.KotlinManager
import com.blaster.data.managers.kotlin.KotlinManagerImpl
import com.blaster.data.managers.printing.PrintingManager
import com.blaster.data.managers.printing.PrintingManagerImpl
import com.blaster.data.managers.statements.StatementsManager
import com.blaster.data.managers.statements.StatementsManagerImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.File
import java.net.URL

val kodein = Kodein {
    bind<InteractorParse>()     with singleton { InteractorParse() }
    bind<StatementsManager>()   with singleton { StatementsManagerImpl() }
    bind<PrintingManager>()     with singleton { PrintingManagerImpl() }
    bind<KotlinManager>()       with singleton { KotlinManagerImpl() }
    bind<InteractorLocation>()  with singleton { InteractorLocation() }
    bind<InteractorPrint>()     with singleton { InteractorPrint() }
    bind<InteractorCommands>()  with singleton { InteractorCommands() }
    bind<InteractorStructs>()   with singleton { InteractorStructs() }
}

class LemApp {
    private val interactorParse: InteractorParse by kodein.instance()
    private val interactorPrint: InteractorPrint by kodein.instance()

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