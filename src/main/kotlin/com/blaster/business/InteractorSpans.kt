package com.blaster.business

import com.blaster.data.paragraphs.*

private val BOLD_REGEX = "'([^']+)'".toRegex()

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

class InteractorSpans {
    // TODO: make it recursive
    fun identifySpans(nodes: List<Node>): List<Node> {
        val result = mutableListOf<Node>()
        for (paragraph in nodes) {
            result.add(paragraph)
            if (paragraph is NodeText) {
                for (struct in paragraph.children) {
                    when (struct) {
                        is StructText -> struct.children.addAll(identifyBoldSpansInText(struct.text))
                        is StructListItem -> {
                            for (child in struct.children) {
                                when (child) {
                                    is StructText -> {
                                        child.children.addAll(identifyBoldSpansInText(child.text))
                                    }
                                    else -> TODO()
                                }
                            }
                        }
                        is StructLink -> {} // nothing
                        else -> TODO()
                    }
                }
            }
        }
        return nodes
    }

    private fun identifyBoldSpansInText(text: String): List<Node> {
        val result = mutableListOf<Node>()
        identifySpansInText(text, BOLD_REGEX) { span: String, isInside: Boolean ->
            if (isInside) {
                result.add(SpanText(span, SpanText.Style.BOLD))
            } else {
                result.add(SpanText(span, SpanText.Style.NORMAL))
            }
        }
        return result
    }
}