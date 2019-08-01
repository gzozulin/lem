package com.blaster.data.managers.parsing

import com.blaster.data.entities.Insert
import com.blaster.data.entities.InsertCode
import com.blaster.data.entities.InsertCommand
import com.blaster.data.entities.InsertComment
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token

class FunctionBodyVisitor(private val tokenStream: CommonTokenStream) : KotlinParserBaseVisitor<List<Insert>>() {
    private val result = ArrayList<Insert>()

    private var isComment = false
    private val currentCodeLines = ArrayList<String>()
    private val currentCommentLines = ArrayList<String>()

    override fun aggregateResult(aggregate: List<Insert>?, nextResult: List<Insert>?): List<Insert> {
        return result
    }

    override fun visitStatements(ctx: KotlinParser.StatementsContext?): List<Insert> {
        val statements = gatherTokens(tokenStream.getTokens(ctx!!.start.tokenIndex + 1, ctx.stop.tokenIndex - 1))
        val lines = trimCommonSpaces(statements.split("[\r]?[\n]".toRegex()))
        for (line in lines) {
            val trimmed = line.trim()
            when {
                trimmed.startsWith("//") -> {
                    flushCurrentCode()
                    if (trimmed.startsWith("// include")) {
                        result.add(InsertCommand(trimmed))
                    } else {
                        currentCommentLines.add(line)
                        flushCurrentComment()
                    }
                }
                trimmed.startsWith("/*") -> { // todo: this marker can be on the same line
                    check(!isComment) { "Comment is already started!" }
                    flushCurrentCode()
                    isComment = true
                    currentCommentLines.add(line)
                }
                trimmed.startsWith("*/") -> { // todo: this marker can be on the same line
                    check(isComment) { "Comment is not started yet!" }
                    currentCommentLines.add(line)
                    flushCurrentComment()
                    isComment = false
                }
                else -> {
                    if (isComment) {
                        currentCommentLines.add(line)
                    } else {
                        currentCodeLines.add(line)
                    }
                }
            }
        }
        flushCurrentCode()
        flushCurrentComment()
        return result
    }

    private fun flushCurrentCode() {
        if (currentCodeLines.isNotEmpty()) {
            var code = ""
            for (codeLine in currentCodeLines) {
                code += codeLine + "\n"
            }
            code = code.dropLast(1)
            result.add(InsertCode(code))
            currentCodeLines.clear()
        }
    }

    private fun flushCurrentComment() {
        if (currentCommentLines.isNotEmpty()) {
            var comment = ""
            for (commentLine in currentCommentLines) {
                comment += commentLine + "\n"
            }
            comment = comment.dropLast(1)
            result.add(InsertComment(comment))
            currentCommentLines.clear()
        }
    }

    private fun gatherTokens(tokens: List<Token>): String {
        var result = ""
        for (token in tokens) {
            result += token.text
        }
        return result
    }

    private fun trimCommonSpaces(lines : List<String>): ArrayList<String> {
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
                index ++
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