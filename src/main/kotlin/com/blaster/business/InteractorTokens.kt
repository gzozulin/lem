package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertText
import com.blaster.data.managers.lexing.LexingManager
import com.blaster.data.managers.parsing.KotlinParser
import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.data.managers.parsing.StatementsParser
import com.blaster.platform.LEM_COMPONENT
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import javax.inject.Inject

class InteractorTokens {
    private val lineRegex = "[\r]?[\n]".toRegex()

    @Inject
    lateinit var lexingManager: LexingManager

    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var interactorCommands: InteractorCommands

    init {
        LEM_COMPONENT.inject(this)
    }

    fun extractStatements(tokenStream: CommonTokenStream, statements: KotlinParser.StatementsContext): List<Insert> {
        val tokens = tokenStream.getTokens(statements.start.tokenIndex + 1, statements.stop.tokenIndex - 1)
        return extractStatementsInternal(tokens)
    }

    fun extractDeclaration(tokenStream: CommonTokenStream, memberDecl: ParserRuleContext): List<Insert> {
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
        return extractStatementsInternal(tokens)
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

    private fun extractStatementsInternal(tokens: List<Token>): List<Insert> {
        val text = tokensToText(tokens)
        val (tokenStream, parser) = lexingManager.provideParserForStatememts(text)
        val statements = parsingManager.locateStatements(tokenStream, parser)
        val result = ArrayList<Insert>()
        for (statement in statements) {
            when (statement) {
                is StatementsParser.DelimitedCommentContext -> {
                    val cleaned = cleanup(statement.text)
                    if (cleaned != null) {
                        result.add(InsertText(cleaned))
                    }
                }
                is StatementsParser.LineCommentContext -> {
                    val cleaned = cleanup(statement.text)
                    if (cleaned != null) {
                        val command = interactorCommands.extractCommand(cleaned)
                        if (command != null) {
                            result.add(command)
                        } else {
                            result.add(InsertText(cleaned))
                        }
                    }
                }
                is StatementsParser.CodeContext -> {
                    val cleaned = cleanup(statement.text)
                    if (cleaned != null) {
                        result.add(InsertCode(cleaned))
                    }
                }
                else -> throw IllegalStateException("UnknownStatement!")
            }
        }
        return result
    }

    private fun cleanup(text: String): String? {
        val noComments = text
            .replace("/*", "")
            .replace("*/", "")
            .replace("//", "")
        if (noComments.isEmpty()) {
            return null
        }
        val lines =  textToLines(noComments)
        if (lines.isEmpty()) {
            return null
        }
        val trimmed = trimCommonSpaces(lines)
        if (trimmed.isEmpty()) {
            return null
        }
        return linesToText(trimmed)
    }

    private fun textToLines(string: String): List<String> {
        return string.split(lineRegex)
    }

    private fun tokensToText(tokens: List<Token>): String {
        var result = ""
        for (token in tokens) {
            result += token.text
        }
        return result
    }

    private fun linesToText(lines: List<String>): String {
        var result = ""
        lines.forEach{ result += it + '\n' }
        return result.dropLast(1)
    }

    private fun trimCommonSpaces(lines: List<String>): List<String> {
        val clean = ArrayList<String>()
        for (line in lines) {
            if (line.isNotBlank()) {
                clean.add(line)
            }
        }
        var min = Int.MAX_VALUE
        for (line in clean) {
            var index = 0
            while (line[index] == ' ') {
                index++
            }
            if (index < min) {
                min = index
            }
        }
        val trimmed = ArrayList<String>()
        for (line in clean) {
            trimmed.add(line.substring(min, line.length))
        }
        return trimmed
    }
}