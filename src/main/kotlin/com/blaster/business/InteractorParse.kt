package com.blaster.business

import com.blaster.data.managers.kotlin.KotlinManager
import com.blaster.data.managers.statements.StatementsManager
import com.blaster.data.nodes.Node
import com.blaster.data.nodes.NodeText
import com.blaster.platform.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.net.URL

private fun String.clearCode() = this.removePrefix("\n").trimIndent()

class InteractorParse {
    private val kotlinManager: KotlinManager            by kodein.instance()
    private val interactorCommands: InteractorCommands  by kodein.instance()
    private val statementsManager: StatementsManager    by kodein.instance()
    private val interactorStructs: InteractorStructs    by kodein.instance()

    // This call will convert a scenario file into a list of nodes. The parameters are self explanatory.
    fun parseScenario(root: File, sourceUrl: URL, scenario: File): List<Node> {
        // First operation of this method is to convert text in the scenario file into a distinct nodes. Paragraphs are separated by the new lines
        val nodes = scenario.readText().lines().map { NodeText(it) }
        // The next task is to apply common procedures for the nodes: identification and application of commands, identification of the structures
        return renderNodes(root, sourceUrl, nodes)
        // #include; def; com.blaster.business.InteractorParse::renderNodes
    }

    // Routine for parsing of the definitions. Accepts the sources url and root and a location of the definition. Returns a list of nodes for this definition (commentaries, code, commands, etc.)
    fun parseDef(root: File, sourceUrl: URL, location: Location): List<Node> {
        // When the definition is located, we extract the code with the help of the ANTLR4
        val definition = kotlinManager.extractDefinition(location)
        // Next step is to split this text onto the commentaries and code snippets. We also format them - removing unused lines, spaces, etc.
        val withoutTabulation = definition.clearCode()
        val statements = statementsManager.extractStatements(withoutTabulation, "kotlin")
        return renderNodes(root, sourceUrl, statements)
        // #include; def; com.blaster.business.InteractorParse::renderNodes
    }

    fun parseDecl(root: File, sourceUrl: URL, location: Location): List<Node> {
        val declaration = kotlinManager.extractDeclaration(location)
        val withoutTabulation = declaration.clearCode()
        val statements = statementsManager.extractStatements(withoutTabulation, "kotlin")
        return renderNodes(root, sourceUrl, statements)
    }

    private fun renderNodes(root: File, sourceUrl: URL, nodes: List<Node>): List<Node> {
        val withCommands = interactorCommands.identifyCommands(root, sourceUrl, nodes)
        val commandsApplied = interactorCommands.applyCommands(root, sourceUrl, withCommands)
        return interactorStructs.identifyStructs(commandsApplied)
    }

    fun parseGlsl(root: File, sourceUrl: URL, location: Location): List<Node> {
        val glsl = location.file.readText()
        val withoutTabulation = glsl.clearCode()
        val statements = statementsManager.extractStatements(withoutTabulation, "c")
        return renderNodes(root, sourceUrl, statements)
    }
}