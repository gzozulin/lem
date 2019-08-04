package com.blaster.data.managers.parsing.extractors

import org.antlr.v4.runtime.Token

fun gatherTokens(tokens: List<Token>): String {
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

fun codeLines(code: String): ArrayList<String> {
    return trimCommonSpaces(code.split("[\r]?[\n]".toRegex()))
}