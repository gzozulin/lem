package com.blaster.business

import com.blaster.data.paragraphs.ParagraphCode
import com.blaster.data.paragraphs.ParagraphText
import io.reactivex.Observable

private val LINE_REGEX = "[\r]?[\n]+".toRegex()

class InteractorFormat {
    fun textToParagraphs(text: String): List<ParagraphText> = Observable.just(text)
        .flatMap { Observable.fromIterable(it.split(LINE_REGEX)) }
        .map { ParagraphText(it) }
        .toList()
        .blockingGet()

    fun formatCode(code: String): ParagraphCode = Observable.just(code)
        .map { textToLines(it) }
        .map { trimCommonSpaces(it) }
        .map { linesToText(it) }
        .map { ParagraphCode(it) }
        .blockingFirst()

    private fun textToLines(string: String): List<String> {
        return string.split(LINE_REGEX)
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