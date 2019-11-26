package com.blaster.business

import com.blaster.data.managers.kotlin.KotlinManager
import com.blaster.data.managers.statements.StatementsManager
import com.blaster.data.paragraphs.Paragraph
import com.blaster.platform.LEM_COMPONENT
import java.io.File
import javax.inject.Inject

class InteractorParse {
    @Inject
    lateinit var interactorLocation: InteractorLocation

    @Inject
    lateinit var interactorCommands: InteractorCommands

    @Inject
    lateinit var statementsManager: StatementsManager

    @Inject
    lateinit var interactorFormat: InteractorFormat

    @Inject
    lateinit var kotlinManager: KotlinManager

    init {
        LEM_COMPONENT.inject(this)
    }

    // This call will convert a scenario file into a list of paragraphs. The parameters are self explanatory.
    fun parseScenario(sourceRoot: File, scenario: File): List<Paragraph> {
        // First operation of this method is to convert text in the scenario file into a distinct paragraphs. Paragraphs are separated by the new lines
        val paragraphs = interactorFormat.textToParagraphs(scenario.readText())
        // The next operation is to identify commands in those paragraphs if any
        val withCommands = interactorCommands.identifyCommands(paragraphs)
        // And finally, if we found any commands - we will apply them to the current result
        return interactorCommands.applyCommands(sourceRoot, withCommands)
    }

    // Routine for parsing of the definitions. Accepts the sources root and a path to a definition. Returns a list of paragraphs with this definition commentaries and code snippets
    fun parseDef(sourceRoot: File, path: String): List<Paragraph> {
        // First thing first - we need to find the actual location of the definition - file, class, etc.
        val location = interactorLocation.locate(sourceRoot, path)
        // When the definition is located, we extract the code with the help of the ANTLR4
        val definition = kotlinManager.extractDefinition(location)
        // Next step is to split this text onto the commentaries and code snippets. We also format them - removing unused lines, spaces, etc.
        val withoutTabulation = interactorFormat.removeCommonTabulation(definition)
        val statements = statementsManager.extractStatements(withoutTabulation)
        // After formatting is done, we want to find the commands among the paragraphs if any
        val withCommands = interactorCommands.identifyCommands(statements)
        // And finally, we apply the commands and return the result
        return interactorCommands.applyCommands(sourceRoot, withCommands)
    }

    fun parseDecl(sourceRoot: File, path: String): List<Paragraph> {
        val location = interactorLocation.locate(sourceRoot, path)
        val declarations = kotlinManager.extractDeclaration(location)
        val withoutTabulation = mutableListOf<String>()
        declarations.forEach { withoutTabulation.add(interactorFormat.removeCommonTabulation(it)) }
        val statements = mutableListOf<Paragraph>()
        declarations.forEach { statements.addAll(statementsManager.extractStatements(it)) }
        return interactorCommands.applyCommands(sourceRoot, statements)
    }
}