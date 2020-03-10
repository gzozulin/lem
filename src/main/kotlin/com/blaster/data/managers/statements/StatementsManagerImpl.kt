package com.blaster.data.managers.statements

import com.blaster.data.nodes.Node
import com.blaster.data.nodes.NodeCode
import com.blaster.data.nodes.NodeText
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

class StatementsManagerImpl : StatementsManager {
    override fun extractStatements(code: String, lang: String): List<Node> {
        val stream = CommonTokenStream(StatementsLexer(CharStreams.fromString(code)))
        val parser = StatementsParser(stream)
        val statements = locateStatements(parser)
        val result = mutableListOf<Node>()
        for (statement in statements) {
            if (statement.text.contains("todo:")) {
                continue
            }
            when (statement) {
                is StatementsParser.DelimitedCommentContext -> {
                    result.add(NodeText(statement.text.removePrefix("/*").removeSuffix("*/").trim()))
                }
                is StatementsParser.LineCommentContext -> {
                    result.add(NodeText(statement.text.removePrefix("//").trim()))
                }
                is StatementsParser.CodeContext -> {
                    // todo: unfortunately, Statements grammar considers everything, not included into a comment being a part of the code, that includes newlines of the comments as well
                    result.add(NodeCode(statement.text.removePrefix("\n").trimEnd(), lang))
                }
                else -> throw IllegalStateException("Unknown statement!")
            }
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