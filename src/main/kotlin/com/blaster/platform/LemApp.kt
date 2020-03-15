package com.blaster.platform

import com.blaster.business.*
import com.blaster.data.managers.kotlin.KotlinManager
import com.blaster.data.managers.kotlin.KotlinManagerImpl
import com.blaster.data.managers.kotlin.ReportedError
import com.blaster.data.managers.printing.PrintingManager
import com.blaster.data.managers.printing.PrintingManagerImpl
import com.blaster.data.managers.statements.StatementsManager
import com.blaster.data.managers.statements.StatementsManagerImpl
import kotlinx.coroutines.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.File
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.system.measureNanoTime

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

private val interactorParse: InteractorParse by kodein.instance()
private val interactorPrint: InteractorPrint by kodein.instance()
private val kotlinManager: KotlinManager by kodein.instance()

private data class Scenario(val root: File, val scenarioFile: File, val outputFile: File, val sourceUrl: URL)

private fun fetchScenarios(root: String, url: String, scenarios: String = "scenarios", articles: String = "articles"): List<Scenario> {
    val result = mutableListOf<Scenario>()
    val scenariosDir = File("$root/$scenarios")
    val outputDir = File("$root/$articles")
    scenariosDir.list()!!.forEach { filename ->
        val sourcesUrl = URL("https", "github.com", url)
        val rootFile = File(root)
        val scenarioFile = File(scenariosDir, filename)
        val outputFile = File(outputDir, "$filename.html")
        result.add(Scenario(rootFile, scenarioFile, outputFile, sourcesUrl))
    }
    return result
}

private suspend fun renderScenario(scenario: Scenario) = withContext(Dispatchers.Default) {
    val parsed = interactorParse.parseScenario(scenario.root, scenario.sourceUrl, scenario.scenarioFile)
    interactorPrint.printArticle(scenario.outputFile, parsed)
    println("Scenario parsed: $scenario")
}

// This is an application main entry point. Here we define all the projects we want to process into the articles.
fun main() {
    val scenarios = mutableListOf<Scenario>()
    scenarios.addAll(fetchScenarios("./",         "/madeinsoviets/lem/blob/develop/"))
    scenarios.addAll(fetchScenarios("../blaster", "/madeinsoviets/blaster/blob/master/"))
    val seconds = TimeUnit.NANOSECONDS.toMillis(measureNanoTime {
        try {
            runBlocking {
                val awaits = mutableListOf<Deferred<Unit>>()
                for (scenario in scenarios) {
                    awaits.add(async { renderScenario(scenario) })
                }
                awaits.awaitAll()
            }
        } catch (th : Throwable) {
            th.printStackTrace()
        } finally {
            for (reported in kotlinManager.reportErrors()) {
                println("--------------------------------------------------------------")
                println("For file ${reported.key} found following errors:")
                val errors = reported.value.sortedWith(compareBy<ReportedError> { it.line }.thenBy { it.char })
                for (err in errors) {
                    println(err)
                }
            }
            println("--------------------------------------------------------------")
        }
    }) / 1000f
    println("Done in $seconds seconds")
}