package com.blaster.data.managers.parsing

import com.blaster.data.entities.Insert
import com.blaster.data.entities.InsertCode
import com.blaster.data.entities.InsertText
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token

// todo combine comments, code
// todo will skip comments if line in between

class FunctionBodyVisitor(private val tokenStream: CommonTokenStream) : KotlinParserBaseVisitor<List<Insert>>() {
    private val result = ArrayList<Insert>()

    override fun aggregateResult(aggregate: List<Insert>?, nextResult: List<Insert>?): List<Insert> {
        return result
    }

    override fun visitStatements(ctx: KotlinParser.StatementsContext?): List<Insert> {
        val statements = gatherTokens(tokenStream.getTokens(ctx!!.start.tokenIndex + 1, ctx.stop.tokenIndex - 1))
        val lines = trimLines(statements.split("[\r]?[\n]".toRegex()))
        var isComment = false
        var currentComment = ""
        var currentCode = ""
        for (line in lines) {
            when {
                line.startsWith("//") -> {
                    if (currentCode.isNotEmpty()) {
                        result.add(InsertCode(currentCode))
                        currentCode = ""
                    }
                    currentComment += line
                }
                line.startsWith("/*") -> {
                    if (currentCode.isNotEmpty()) {
                        result.add(InsertCode(currentCode))
                        currentCode = ""
                    }
                    isComment = true
                    currentComment += line
                }
                line.startsWith("*/") -> {
                    currentComment += line
                    if (currentComment.isNotEmpty()) {
                        result.add(InsertText(currentComment))
                        currentComment = ""
                    }
                    isComment = false
                }
                else -> {
                    if (isComment) {
                        currentComment += line
                    } else {
                        currentCode += line
                    }
                }
            }
        }
        return result
    }

    private fun gatherTokens(tokens: List<Token>): String {
        var result = ""
        for (token in tokens) {
            result += token.text
        }
        return result
    }

    private fun trimLines(lines : List<String>): ArrayList<String> {
        var min = Int.MAX_VALUE
        for (line in lines) {
            var index = 0
            while (line[index] == ' ') {
                index ++
            }
            if (index < min) {
                min = index
            }
        }
        val result = ArrayList<String>()
        for (line in lines) {
            result.add(line.substring(min, line.length))
        }
        return result
    }
}