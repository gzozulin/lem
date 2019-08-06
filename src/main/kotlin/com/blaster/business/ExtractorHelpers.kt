package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertText
import org.antlr.v4.runtime.Token

fun tokensToText(tokens: List<Token>): String {
    var result = ""
    for (token in tokens) {
        result += token.text
    }
    return result
}

private val LINE_REGEX = "[\r]?[\n]".toRegex()
fun textToLines(string: String): List<String> {
    return string.split(LINE_REGEX)
}

fun linesToText(lines: List<String>): String {
    var result = ""
    lines.forEach{ result += it + '\n' }
    return result.dropLast(1)
}

fun trimCommonSpaces(lines: List<String>): List<String> {
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

private val singleLineRegex = "^//(.*)\$".toRegex()
private val delimitedRegex = "^/\\*((.*\\n?)+)\\*/\$".toRegex()

fun commentToText(comment: String): Insert {
    val singleLineMatch = singleLineRegex.find(comment)
    if (singleLineMatch != null) {
        return InsertText(singleLineMatch.groups[1]!!.value)
    }
    val delimitedMatch = delimitedRegex.find(comment)
    if (delimitedMatch != null) {
        var cleaned = delimitedMatch.groups[1]!!.value
        cleaned = cleaned.drop(1) // first '\n'
        cleaned = cleaned.dropLast(1) // trailing '\n'
        return InsertText(cleaned)
    }
    throw IllegalStateException("At least one of them should match!")
}