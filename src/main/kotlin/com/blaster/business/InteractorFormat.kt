package com.blaster.business

import com.blaster.data.paragraphs.*

private val LINE_REGEX = "[\r*\n]+".toRegex()
private val SPAN_REGEX = "'([^']+)'".toRegex()

class InteractorFormat {
    fun textToParagraphs(text: String): List<ParagraphText> = text.split(LINE_REGEX)
        .map { ParagraphText(it) }

    fun removeCommonTabulation(code: String): String {
        val lines = textToLines(code)
        val noEmpty = removeEmpty(lines)
        val noCommonSpace = trimCommonSpaces(noEmpty)
        return linesToText(noCommonSpace)
    }

    fun identifySpans(paragraphs: List<Paragraph>): List<Paragraph> {
        val result = mutableListOf<Paragraph>()
        for (paragraph in paragraphs) {
            result.add(paragraph)
            if (paragraph is ParagraphText) {
                for (struct in paragraph.children) {
                    when (struct) {
                        is StructText -> struct.children.addAll(identifySpansInText(struct.text))
                        is StructListItem -> struct.children.addAll(identifySpansInText(struct.item))
                        else -> TODO()
                    }
                }
            }
        }
        return paragraphs
    }

    private fun identifySpansInText(text: String): List<Paragraph> {
        val result = mutableListOf<Paragraph>()
        var remainder = text
        var match = SPAN_REGEX.find(remainder)
        while (match != null) {
            val whole = match.groups[0]
            val value = match.groups[1]
            val normal = remainder.substring(0, whole!!.range.first)
            val bold = value!!.value
            result.add(SpanText(normal, SpanText.Style.NORMAL))
            result.add(SpanText(bold, SpanText.Style.BOLD))
            remainder = remainder.substring(whole.range.last + 1, remainder.length)
            match = SPAN_REGEX.find(remainder)
        }
        if (remainder.isNotEmpty()) {
            result.add(SpanText(remainder, SpanText.Style.NORMAL))
        }
        return result
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