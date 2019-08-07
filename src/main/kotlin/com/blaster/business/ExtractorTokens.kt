package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.inserts.InsertText
import io.reactivex.Observable
import org.antlr.v4.runtime.Token

class ExtractorTokens(private val tokens: List<Token>) {
    private val result = ArrayList<Insert>()

    private val currentCodeLines = ArrayList<String>()
    private val currentCommentLines = ArrayList<String>()

    private val lineRegex = "[\r]?[\n]".toRegex()

    private var isComment = false

    fun extractStatements(): List<Insert> {
        val lines = Observable.just(tokens)
            .map { tokensToText(it) }
            .map { textToLines(it) }
            .map { trimCommonSpaces(it) }
            .blockingFirst()
        Observable.fromIterable(lines)
            .doOnNext { enforceProperComments(it) }
            .subscribe()
        for (line in lines) {
            val trimmed = line.trim()
            when {
                trimmed.startsWith("//") -> {
                    flushCurrentCode()
                    if (trimmed.startsWith("// include")) {
                        result.add(InsertCommand(trimmed))
                    } else {
                        currentCommentLines.add(trimmed.removePrefix("// "))
                        flushCurrentComment()
                    }
                }
                trimmed.startsWith("/*") -> {
                    check(!isComment) { "Comment is already started!" }
                    flushCurrentCode()
                    isComment = true
                }
                trimmed.startsWith("*/") -> {
                    check(isComment) { "Comment is not started yet!" }
                    flushCurrentComment()
                    isComment = false
                }
                else -> {
                    if (isComment) {
                        currentCommentLines.add(trimmed)
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

    private fun enforceProperComments(line: String) {
        if (line.contains("/*") || line.contains("*/")) {
            check(line.trim() == "/*" || line.trim() == "*/") { "We do not support comments in between of the lines!" }
        }
    }

    private fun flushCurrentCode() {
        if (currentCodeLines.isNotEmpty()) {
            result.add(InsertCode(linesToText(currentCodeLines)))
            currentCodeLines.clear()
        }
    }

    private fun flushCurrentComment() {
        if (currentCommentLines.isNotEmpty()) {
            result.add(InsertText(linesToText(currentCommentLines)))
            currentCommentLines.clear()
        }
    }

    private fun tokensToText(tokens: List<Token>): String {
        var result = ""
        for (token in tokens) {
            result += token.text
        }
        return result
    }

    private fun textToLines(string: String): List<String> {
        return string.split(lineRegex)
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