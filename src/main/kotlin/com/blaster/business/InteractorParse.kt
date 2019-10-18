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

    fun parseScenario(sourceRoot: File, scenario: File): List<Paragraph> =
        Observable.just(scenario)
            .map { interactorFormat.textToParagraphs(scenario.readText()) }
            .map { interactorCommands.identifyCommands(it) }
            .map { interactorCommands.applyCommands(sourceRoot, it) }
            .blockingFirst()

    fun parseDef(sourceRoot: File, path: String): List<Paragraph> {
        val location = interactorLocation.locate(sourceRoot, path)
        val definition = kotlinManager.extractDefinition(location)
        val paragraphs = statementsManager.extractStatements(definition)
        val formatted = formatParagraphs(paragraphs)
        val withCommands = interactorCommands.identifyCommands(formatted)
        return interactorCommands.applyCommands(sourceRoot, withCommands)
    }

    fun parseDecl(sourceRoot: File, path: String): List<Paragraph> {
        val location = interactorLocation.locate(sourceRoot, path)
        val declarations = kotlinManager.extractDeclaration(location)
        val paragraphs = Observable.fromIterable(declarations)
            .flatMap { Observable.fromIterable(statementsManager.extractStatements(it)) }
            .toList()
            .blockingGet()
        val formatted = formatParagraphs(paragraphs)
        val withCommands = interactorCommands.identifyCommands(formatted)
        return interactorCommands.applyCommands(sourceRoot, withCommands)
    }

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