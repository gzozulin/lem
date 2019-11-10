package com.blaster.data.managers.statements

import com.blaster.data.paragraphs.Paragraph
import com.blaster.data.paragraphs.ParagraphCode
import com.blaster.data.paragraphs.ParagraphText
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

class StatementsManagerImpl : StatementsManager {
    private val statementsCache = HashMap<String, Pair<CommonTokenStream, StatementsParser>>()

    override fun extractStatements(code: String): List<Paragraph> {
        val (_, parser) = provideParserForStatememts(code)
        parser.reset()
        val statements = locateStatements(parser)
        val result = ArrayList<Paragraph>()
        for (statement in statements) {
            when (statement) {
                is StatementsParser.DelimitedCommentContext -> {
                    result.add(ParagraphText(statement.text.removePrefix("/*").removeSuffix("*/").trim()))
                }
                is StatementsParser.LineCommentContext -> {
                    result.add(ParagraphText(statement.text.removePrefix("//").trim()))
                }
                is StatementsParser.CodeContext -> {
                    // todo: unfortunately, Statements grammar considers everything, not included into a comment being a part of the code, that includes newlines of the comments as well
                    result.add(ParagraphCode(statement.text.removePrefix("\n").trimEnd()))
                }
                else -> throw IllegalStateException("Unknown statement!")
            }
        }
        return result
    }

    private fun provideParserForStatememts(key: String): Pair<CommonTokenStream, StatementsParser> {
        var result = statementsCache[key]
        if (result == null) {
            val stream = CommonTokenStream(
                StatementsLexer(
                    CharStreams.fromString(
                        key
                    )
                )
            )
            val parser = StatementsParser(stream)
            result = stream to parser
            statementsCache[key] = result
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
}