package com.blaster.business

import com.blaster.data.inserts.Insert
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

    fun parseDef(sourceRoot: File, path: String): List<Insert> {
        val location = interactorLocation.locate(sourceRoot, path)
        val definition = kotlinManager.extractDef(location)
        val inserts = statementsManager.extractStatements(definition)
        val withCommands = interactorCommands.identifyCommands(inserts)
        return interactorCommands.applyCommands(sourceRoot, withCommands)
    }

    fun parseDecl(sourceRoot: File, path: String): List<Insert> {
        val location = interactorLocation.locate(sourceRoot, path)
        val declarations = kotlinManager.extractDecl(location)
        val inserts = Observable.fromIterable(declarations)
            .flatMap { Observable.fromIterable(statementsManager.extractStatements(it)) }
            .toList()
            .blockingGet()
        val withCommands = interactorCommands.identifyCommands(inserts)
        return interactorCommands.applyCommands(sourceRoot, withCommands)
    }
}