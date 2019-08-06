package com.blaster.business

import com.blaster.data.inserts.*
import com.blaster.data.managers.parsing.KotlinParser
import io.reactivex.Observable
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token
import kotlin.collections.ArrayList

class ExtractorStatements(private val tokens: List<Token>) {
    private val result = ArrayList<Insert>()

    private var isComment = false
    private val currentCodeLines = ArrayList<String>()
    private val currentCommentLines = ArrayList<String>()

    fun extractStatements(): List<Insert> {
        val lines = Observable.just(tokens)
            .map { tokensToText(it) }
            .map { textToLines(it) }
            .map { trimCommonSpaces(it) }
            .blockingFirst()
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
            result.add(InsertCode(linesToText(currentCodeLines)))
            currentCodeLines.clear()
        }
    }

    private fun flushCurrentComment() {
        if (currentCommentLines.isNotEmpty()) {
            result.add(commentToText(linesToText(currentCommentLines)))
            currentCommentLines.clear()
        }
    }
}