package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.inserts.InsertComment
import com.blaster.data.managers.lexing.LexingManager
import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.platform.LEM_COMPONENT
import java.lang.UnsupportedOperationException
import javax.inject.Inject

const val INCLUDE_PREFIX = "// include "
const val DEF_PREFIX = "def "
const val DECL_PREFIX = "decl "

class InteractorParse {
    @Inject
    lateinit var interactorLocation: InteractorLocation

    @Inject
    lateinit var lexingManager: LexingManager

    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var interactorComments: InteractorComments

    @Inject
    lateinit var extractorDeclarations: ExtractorDeclarations

    @Inject
    lateinit var extractorStatements: ExtractorStatements

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseDef(path: String): List<Insert> {
        val location = interactorLocation.locate(path)
        val (tokenStream, parser) = lexingManager.provideParser(location.file)
        parser.reset()
        val statements = when (location) {
            is LocationGlobal -> parsingManager.locateGlobalMethodStatements(tokenStream, parser, location)
            is LocationMember -> parsingManager.locateMemberMethodStatements(tokenStream, parser, location)
            else -> throw UnsupportedOperationException()
        }
        val inserts = extractorStatements.extractStatements(tokenStream, statements)
        return processInserts(inserts)
    }

    private fun parseDecl(path: String): List<Insert> {
        val location = interactorLocation.locate(path)
        val (tokenStream, parser) = lexingManager.provideParser(location.file)
        parser.reset()
        val inserts = when (location) {
            is LocationGlobal -> {
                val declaration = parsingManager.locateGlobalMethodDecl(tokenStream, parser, location)
                extractorDeclarations.extractDeclarations(tokenStream, declaration)
            }
            is LocationMember -> {
                val declaration = parsingManager.locateMemberDecl(tokenStream, parser, location)
                extractorDeclarations.extractDeclarations(tokenStream, declaration)
            }
            is LocationClass -> {
                val result = ArrayList<Insert>()
                val declarations = parsingManager.locateClassDecl(tokenStream, parser, location)
                declarations.forEach { result.addAll(extractorDeclarations.extractDeclarations(tokenStream, it)) }
                result
            }
            else -> throw UnsupportedOperationException()
        }
        return processInserts(inserts)
    }

    private fun processInserts(inserts: List<Insert>) : List<Insert> {
        val result = ArrayList<Insert>()
        for (insert in inserts) {
            when (insert) {
                is InsertCommand -> {
                    result.addAll(processCommand(insert.command))
                }
                is InsertComment -> {
                    result.add(interactorComments.processComment(insert.comment))
                }
                else -> result.add(insert)
            }
        }
        return result
    }

    private fun processCommand(command: String): List<Insert> {
        check(command.startsWith(INCLUDE_PREFIX)) { "Expected command is not started with expected prefix!" }
        val include = command.removePrefix(INCLUDE_PREFIX)
        return processInclude(include)
    }

    private fun processInclude(include: String): List<Insert> {
        if (include.startsWith(DECL_PREFIX)) {
            val decl = include.removePrefix(DECL_PREFIX)
            return parseDecl(decl)
        }
        if (include.startsWith(DEF_PREFIX)) {
            val def = include.removePrefix(DEF_PREFIX)
            return parseDef(def)
        }
        throw IllegalStateException("Wtf??")
    }
}