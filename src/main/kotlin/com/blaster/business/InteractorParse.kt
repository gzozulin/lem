package com.blaster.business

import com.blaster.data.paragraphs.Paragraph
import com.blaster.data.paragraphs.ParagraphCode
import com.blaster.data.paragraphs.ParagraphText
import com.blaster.data.managers.kotlin.KotlinManager
import com.blaster.data.managers.statements.StatementsManager
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
    lateinit var kotlinManager: KotlinManager

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseScenario(sourceRoot: File, scenario: File): List<Paragraph> {
        val inserts = statementsManager.extractStatements(scenario.readText())
        val withCommands = interactorCommands.identifyCommands(inserts)
        val textAndCommands = Observable.fromIterable(withCommands)
            .map {
                when (it) {
                    is ParagraphCode -> ParagraphText(it.code) else -> it // converting all "code" to just text
                }
            }
            .toList()
            .blockingGet()
        return interactorCommands.applyCommands(sourceRoot, textAndCommands)
    }

    fun parseDef(sourceRoot: File, path: String): List<Paragraph> {
        val location = interactorLocation.locate(sourceRoot, path)
        val definition = kotlinManager.extractDefinition(location)
        val inserts = statementsManager.extractStatements(definition)
        val withCommands = interactorCommands.identifyCommands(inserts)
        return interactorCommands.applyCommands(sourceRoot, withCommands)
    }

    fun parseDecl(sourceRoot: File, path: String): List<Paragraph> {
        val location = interactorLocation.locate(sourceRoot, path)
        val declarations = kotlinManager.extractDeclaration(location)
        val inserts = Observable.fromIterable(declarations)
            .flatMap { Observable.fromIterable(statementsManager.extractStatements(it)) }
            .toList()
            .blockingGet()
        val withCommands = interactorCommands.identifyCommands(inserts)
        return interactorCommands.applyCommands(sourceRoot, withCommands)
    }
}