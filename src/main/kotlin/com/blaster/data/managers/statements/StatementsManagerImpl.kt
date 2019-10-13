package com.blaster.data.managers.statements

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertText
import com.blaster.data.managers.kotlin.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

private val LINE_REGEX = "[\r]?[\n]".toRegex()

class StatementsManagerImpl : StatementsManager {
    private val cacheForStatements = HashMap<String, Pair<CommonTokenStream, StatementsParser>>()

    override fun extractStatements(code: String): List<Insert> {
        val (_, parser) = provideParserForStatememts(code)
        parser.reset()
        val statements = locateStatements(parser)
        val result = ArrayList<Insert>()
        for (statement in statements) {
            val cleaned = cleanup(statement.text)
            if (cleaned != null) {
                when (statement) {
                    is StatementsParser.DelimitedCommentContext -> {
                        result.add(InsertText(cleaned))
                    }
                    is StatementsParser.CodeContext -> {
                        result.add(InsertCode(cleaned))
                    }
                    is StatementsParser.LineCommentContext -> {
                        result.add(InsertText(cleaned))
                    }
                    else -> throw IllegalStateException("UnknownStatement!")
                }
            }
        }
        return result
    }

    private fun provideParserForStatememts(key: String): Pair<CommonTokenStream, StatementsParser> {
        var result = cacheForStatements[key]
        if (result == null) {
            val stream = CommonTokenStream(StatementsLexer(CharStreams.fromString(key)))
            val parser = StatementsParser(stream)
            result = stream to parser
            cacheForStatements[key] = result
        }
        return result
    }

    private fun locateStatements(parser: StatementsParser): List<ParserRuleContext> {
        val result = ArrayList<ParserRuleContext>()
        object : StatementsBaseVisitor<Unit>() {
            override fun visitLineComment(ctx: StatementsParser.LineCommentContext?) {
                result.add(ctx!!)
            }

            override fun visitDelimitedComment(ctx: StatementsParser.DelimitedCommentContext?) {
                result.add(ctx!!)
            }

            override fun visitCode(ctx: StatementsParser.CodeContext?) {
                result.add(ctx!!)
            }
        }.visitStatements(parser.statements())
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
        return string.split(LINE_REGEX)
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