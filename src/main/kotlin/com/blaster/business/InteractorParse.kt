package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.managers.lexing.LexingManager
import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.platform.LEM_COMPONENT
import javax.inject.Inject

class InteractorParse {
    @Inject
    lateinit var interactorLocation: InteractorLocation

    @Inject
    lateinit var lexingManager: LexingManager

    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var interactorStatements: InteractorStatements

    @Inject
    lateinit var interactorDeclarations: InteractorDeclarations

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseDef(path: String): List<Insert> {
        val location = interactorLocation.locate(path)
        val (tokenStream, parser) = lexingManager.provideParserForKotlin(location.file)
        parser.reset()
        val statements = when (location) {
            is LocationGlobal -> parsingManager.locateGlobalMethodStatements(tokenStream, parser, location)
            is LocationMember -> parsingManager.locateMemberMethodStatements(tokenStream, parser, location)
            else -> throw UnsupportedOperationException()
        }
        val inserts = interactorStatements.extractStatements(tokenStream, statements)
        return processCommands(inserts)
    }

    private fun parseDecl(path: String): List<Insert> {
        val location = interactorLocation.locate(path)
        val (tokenStream, parser) = lexingManager.provideParserForKotlin(location.file)
        parser.reset()
        val declarations = when (location) {
            is LocationGlobal -> listOf(parsingManager.locateGlobalMethodDecl(tokenStream, parser, location))
            is LocationMember -> listOf(parsingManager.locateMemberDecl(tokenStream, parser, location))
            is LocationClass -> parsingManager.locateClassDecls(tokenStream, parser, location)
            else -> throw UnsupportedOperationException()
        }
        val inserts = ArrayList<Insert>()
        for (declaration in declarations) {
            inserts.addAll(interactorDeclarations.extractDeclaration(tokenStream, declaration))
        }
        return processCommands(inserts)
    }

    private fun processCommands(inserts: List<Insert>): List<Insert> {
        if (inserts.isEmpty()) {
            return inserts
        }
        val mutableList = ArrayList(inserts)
        val iterator = mutableList.listIterator()
        while (iterator.hasNext()) {
            val insert = iterator.next()
            if (insert is InsertCommand) {
                when (insert.type) {
                    InsertCommand.Type.INCLUDE -> {
                        when (insert.subcommand) {
                            SUBCOMMAND_DECL -> {
                                insert.children.addAll(parseDecl(insert.argument))
                            }
                            SUBCOMMAND_DEF -> {
                                insert.children.addAll(parseDef(insert.argument))
                            }
                        }
                    }
                    InsertCommand.Type.OMIT -> {
                        check(iterator.hasNext()) { "What to omit??" }
                        iterator.remove()
                        iterator.next()
                        iterator.remove()
                    }
                }
            }
        }
        return mutableList
    }
}