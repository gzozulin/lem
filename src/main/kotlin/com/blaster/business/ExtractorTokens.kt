package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.inserts.InsertText
import com.blaster.data.managers.lexing.LexingManager
import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.data.managers.parsing.StatementsParser
import com.blaster.platform.LEM_COMPONENT
import org.antlr.v4.runtime.Token
import java.lang.IllegalStateException
import javax.inject.Inject

class ExtractorTokens {
    private val lineRegex = "[\r]?[\n]".toRegex()

    @Inject
    lateinit var lexingManager: LexingManager

    @Inject
    lateinit var parsingManager: ParsingManager

    init {
        LEM_COMPONENT.inject(this)
    }

    fun extractStatements(tokens: List<Token>): List<Insert> {
        val text = tokensToText(tokens)
        val (tokenStream, parser) = lexingManager.provideParserForStatememts(text)
        val statements = parsingManager.locateStatements(tokenStream, parser)
        val result = ArrayList<Insert>()
        for (statement in statements) {
            when (statement) {
                is StatementsParser.MultiLineCommentContext -> {
                    val cleaned = cleanup(statement.meat().text)
                    if (cleaned != null) {
                        result.add(InsertText(cleaned))
                    }
                }
                is StatementsParser.SingleLineCommentContext -> {
                    val cleaned = cleanup(statement.meat().text)
                    if (cleaned != null) {
                        if (cleaned.startsWith("include")) {
                            result.add(InsertCommand(cleaned))
                        } else {
                            result.add(InsertText(cleaned))
                        }
                    }
                }
                is StatementsParser.CodeContext -> {
                    val cleaned = cleanup(statement.meat().text)
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
        val lines =  textToLines(text)
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