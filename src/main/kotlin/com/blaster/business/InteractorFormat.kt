package com.blaster.business

import com.blaster.data.nodes.*

private val LINE_REGEX = "[\r*\n]+".toRegex()

class InteractorFormat {
    fun textToParagraphs(text: String): List<NodeText> = text.split(LINE_REGEX)
        .map { NodeText(it) }

    fun removeCommonTabulation(code: String): String {
        val lines = textToLines(code)
        val noEmpty = removeEmpty(lines)
        val noCommonSpace = trimCommonSpaces(noEmpty)
        return linesToText(noCommonSpace)
    }

    private fun textToLines(string: String): List<String> {
        return string.split(LINE_REGEX)
    }

    private fun linesToText(lines: List<String>): String {
        var result = ""
        lines.forEach{ result += it + '\n' }
        return result.dropLast(1)
    }

    private fun removeEmpty(lines: List<String>) = lines
        .filter { it.isNotBlank() }

    private fun trimCommonSpaces(lines: List<String>): List<String> {
        var min = Int.MAX_VALUE
        for (line in lines) {
            var index = 0
            while (line[index] == ' ') {
                index++
            }
            if (index < min) {
                min = index
            }
        }
        val trimmed = ArrayList<String>()
        for (line in lines) {
            trimmed.add(line.substring(min, line.length))
        }
        return trimmed
    }
}