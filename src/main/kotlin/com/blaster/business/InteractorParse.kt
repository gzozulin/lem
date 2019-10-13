package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.inserts.SUBCOMMAND_DECL
import com.blaster.data.inserts.SUBCOMMAND_DEF
import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.data.managers.traversing.KotlinParser
import com.blaster.data.managers.traversing.TraversingManager
import com.blaster.platform.LEM_COMPONENT
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.io.File
import javax.inject.Inject

class InteractorParse {
    @Inject
    lateinit var interactorLocation: InteractorLocation

    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var traversingManager: TraversingManager

    @Inject
    lateinit var interactorTokens: InteractorTokens

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseDef(sourceRoot: File, path: String): List<Insert> {
        val location = interactorLocation.locate(sourceRoot, path)
        val (tokenStream, parser) = parsingManager.provideParserForKotlin(location.file)
        parser.reset()
        val statements = when (location) {
            is LocationGlobal -> traversingManager.locateGlobalMethodStatements(tokenStream, parser, location)
            is LocationMember -> traversingManager.locateMemberMethodStatements(tokenStream, parser, location)
            else -> throw UnsupportedOperationException()
        }
        val inserts = extractStatements(tokenStream, statements)
        return processCommands(sourceRoot, inserts)
    }

    private fun parseDecl(sourceRoot: File, path: String): List<Insert> {
        val location = interactorLocation.locate(sourceRoot, path)
        val (tokenStream, parser) = parsingManager.provideParserForKotlin(location.file)
        parser.reset()
        val declarations = when (location) {
            is LocationGlobal -> listOf(traversingManager.locateGlobalMethodDecl(tokenStream, parser, location))
            is LocationMember -> listOf(traversingManager.locateMemberDecl(tokenStream, parser, location))
            is LocationClass -> traversingManager.locateClassDecls(tokenStream, parser, location)
            else -> throw UnsupportedOperationException()
        }
        val inserts = ArrayList<Insert>()
        for (declaration in declarations) {
            inserts.addAll(extractDeclaration(tokenStream, declaration))
        }
        return processCommands(sourceRoot, inserts)
    }

    private fun extractStatements(tokenStream: CommonTokenStream, statements: KotlinParser.StatementsContext): List<Insert> {
        val tokens = tokenStream.getTokens(statements.start.tokenIndex + 1, statements.stop.tokenIndex - 1)
        return interactorTokens.extractTokens(tokens)
    }

    private fun extractDeclaration(tokenStream: CommonTokenStream, memberDecl: ParserRuleContext): List<Insert> {
        val lastToken = when (memberDecl) {
            is KotlinParser.ClassDeclarationContext    -> tokenStream.get(memberDecl.classBody().start.tokenIndex - 1)
            is KotlinParser.FunctionDeclarationContext -> tokenStream.get(memberDecl.functionBody().start.tokenIndex - 1)
            is KotlinParser.PropertyDeclarationContext -> memberDecl.stop
            else -> throw UnsupportedOperationException("Unknown type of member!")
        }
        val prevDecl = findPrevDeclaration(tokenStream, memberDecl.start.tokenIndex)
        val tokens = if (prevDecl != null) {
            tokenStream.get(prevDecl.tokenIndex + 1, lastToken.tokenIndex)
        } else {
            tokenStream.get(memberDecl.start.tokenIndex, lastToken.tokenIndex)
        }
        return interactorTokens.extractTokens(tokens)
    }

    private fun findPrevDeclaration(tokenStream: CommonTokenStream, index: Int): Token? {
        var current = index - 1
        while(current >= 0) {
            val token = tokenStream.get(current)
            val text = token.text
            // not hidden, not blank, not new line
            if (token.channel != 1 && !text.isBlank()) {
                return token
            }
            current--
        }
        return null
    }

    private fun processCommands(sourceRoot: File, inserts: List<Insert>): List<Insert> {
        val mutableList = ArrayList(inserts)
        val iterator = mutableList.listIterator()
        while (iterator.hasNext()) {
            val insert = iterator.next()
            if (insert is InsertCommand) {
                when (insert.type) {
                    InsertCommand.Type.INCLUDE -> {
                        when (insert.subcommand) {
                            SUBCOMMAND_DECL -> {
                                insert.children.addAll(parseDecl(sourceRoot, insert.argument))
                            }
                            SUBCOMMAND_DEF -> {
                                insert.children.addAll(parseDef(sourceRoot, insert.argument))
                            }
                        }
                    }
                    InsertCommand.Type.OMIT -> {
                        check(iterator.hasNext()) { "What to omit??" }
                        iterator.remove()
                        iterator.next()
                        iterator.remove()
                    }
                    InsertCommand.Type.INLINE -> {
                        iterator.remove()
                        when (insert.subcommand) {
                            SUBCOMMAND_DECL -> {
                                val declarations = parseDecl(sourceRoot, insert.argument)
                                for (decl in declarations) {
                                    iterator.add(decl)
                                }
                            }
                            SUBCOMMAND_DEF -> {
                                val definitions = parseDef(sourceRoot, insert.argument)
                                for (def in definitions) {
                                    iterator.add(def)
                                }
                            }
                        }
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }
        return mutableList
    }
}