package com.blaster.business

import com.blaster.data.managers.kotlin.KotlinManager
import com.blaster.data.managers.statements.StatementsManager
import com.blaster.data.paragraphs.Paragraph
import com.blaster.data.paragraphs.ParagraphCode
import com.blaster.data.paragraphs.ParagraphText
import com.blaster.platform.LEM_COMPONENT
import io.reactivex.Observable
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
    fun parseScenario(sourceRoot: File, scenario: File): List<Paragraph> = Observable.just(scenario)
            // First operation of this method is to convert text in the scenario file into a distinct paragraphs. Paragraphs are separated by the new lines.
            .map { interactorFormat.textToParagraphs(scenario.readText()) }
            // The next operation is to identify commands in those paragraphs if any
            .map { interactorCommands.identifyCommands(it) }
            // And finally, if we found any commands - we will apply them to the current result
            .map { interactorCommands.applyCommands(sourceRoot, it) }
            .blockingFirst()

    // Routine for parsing of the definitions. Accepts the sources root and a path to a definition. Returns a list of paragraphs with this definition commentaries and code snippets
    fun parseDef(sourceRoot: File, path: String): List<Paragraph> = Observable.just(path)
            // First thing first - we need to find the actual location of the definition - file, class, etc.
            .map { interactorLocation.locate(sourceRoot, it) }
            // When the definition is located, we extract the code with the help of the ANTLR4
            .map { kotlinManager.extractDefinition(it) }
            // Next step is to split this text onto the commentaries and code snippets. We also format them - removing unused lines, spaces, etc.
            .map { statementsManager.extractStatements(it) }
            .map { formatParagraphs(it) }
            // After formatting is done, we want to find the commands among the paragraphs if any
            .map { interactorCommands.identifyCommands(it) }
            // And finally, we apply the commands and return the result
            .map { interactorCommands.applyCommands(sourceRoot, it) }
            .blockingFirst()

    fun parseDecl(sourceRoot: File, path: String): List<Paragraph> = Observable.just(path)
            .map { interactorLocation.locate(sourceRoot, it) }
            .map { kotlinManager.extractDeclaration(it) }
            .flatMap { decls -> Observable.fromIterable(decls)
                .flatMap { Observable.fromIterable(statementsManager.extractStatements(it)) } }
            .toList()
            .map { formatParagraphs(it) }
            .map { formatParagraphs(it) }
            .map { interactorCommands.applyCommands(sourceRoot, it) }
            .blockingGet()

    private fun formatParagraphs(paragraphs: List<Paragraph>): List<Paragraph> = Observable.fromIterable(paragraphs)
        .flatMap {
            when(it) {
                is ParagraphCode -> Observable.just(interactorFormat.formatCode(it.code))
                is ParagraphText -> Observable.fromIterable(interactorFormat.textToParagraphs(it.text))
                else -> throw IllegalStateException("Unknown type of paragraph!")
            }
        }
        .toList()
        .blockingGet()
}