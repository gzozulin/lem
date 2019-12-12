package com.blaster.business

fun identifySpansInText(
    text: String, pattern: Regex, action: (span: String, isInside: Boolean) -> Unit) {
    var remainder = text
    var match = pattern.find(remainder)
    while (match != null) {
        val whole = match.groups[0]
        val value = match.groups[1]
        val outside = remainder.substring(0, whole!!.range.first)
        val inside = value!!.value
        action(outside, false)
        action(inside, true)
        remainder = remainder.substring(whole.range.last + 1, remainder.length)
        match = pattern.find(remainder)
    }
    if (remainder.isNotEmpty()) {
        action(remainder, false)
    }
}

private val CSV_PATTERN = ";\\s?".toPattern()
fun splitCsv(text: String) = text.split(CSV_PATTERN)