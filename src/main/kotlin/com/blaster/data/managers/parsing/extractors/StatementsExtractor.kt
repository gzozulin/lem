package com.blaster.data.managers.parsing.extractors

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.inserts.InsertComment
import com.blaster.data.managers.parsing.KotlinParser
import org.antlr.v4.runtime.CommonTokenStream

class StatementsExtractor {
    private val result = ArrayList<Insert>()

    private var isComment = false
    private val currentCodeLines = ArrayList<String>()
    private val currentCommentLines = ArrayList<String>()

    fun extractStatements(tokenStream: CommonTokenStream, ctx: KotlinParser.StatementsContext?): List<Insert> {
        val statements = gatherTokens(
            tokenStream.getTokens(
                ctx!!.start.tokenIndex + 1,
                ctx.stop.tokenIndex - 1
            )
        )
        val lines = codeLines(statements)
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
}