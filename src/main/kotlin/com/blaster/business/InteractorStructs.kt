package com.blaster.business

import com.blaster.data.nodes.*
import java.net.URL

private val regexListItem   = """~\s*(.+)${'$'}""".toRegex()
private val regexLink       = """\[([^\[]+)\]""".toRegex()
private val regexCite       = """\\([^\\]+)\\""".toRegex()
private val regexBold       = """'([^']+)'""".toRegex()

private fun identifySpansInText(text: String, pattern: Regex, action: (span: String, isInside: Boolean) -> Unit) {
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

class InteractorStructs {
    // This routine will kickstart the process of identification of formatting inside of the TextNode. The identification is based on the regular expressions checks. If a certain regular expression is found, we extract pieces of text with the appropriate formatting
    fun identifyStructs(nodes: List<Node>): List<Node> = nodes.map {
        when (it) {
            is NodeText -> {
                val transformed = mutableListOf<Node>()
                // If the node is TextNode, we are trying to identify list items among the text
                transformed.addAll(identifyListItems(it))
                it.copy(children = transformed)
            }
            else -> it
        }
    }

    private fun identifyListItems(node: NodeText): List<Node> {
        val match = regexListItem.find(node.text)
        return if (match != null) {
            // We can have links inside of the list items
            listOf(StructListItem(identifyLinks(match.groups[1]!!.value)))
        } else {
            // Or inside of any text
            identifyLinks(node.text)
        }
    }

    private fun identifyLinks(text: String): List<Node> {
        val result = mutableListOf<Node>()
        identifySpansInText(text, regexLink) { span: String, isInside: Boolean ->
            val node = if (isInside) {
                listOf(parseLink(span))
            } else {
                // If text is not a part of the link, it still can contain references to the other sources
                identifyCites(span)
            }
            result.addAll(node)
        }
        return result
    }

    private fun identifyCites(text: String): List<Node> {
        val result = mutableListOf<Node>()
        identifySpansInText(text, regexCite) { span: String, isInside: Boolean ->
            val node = if (isInside) {
                StructCite(span)
            } else {
                // If we have just a piece of a text, we want to identify spans if any
                StructText(identifySpans(span))
            }
            result.add(node)
        }
        return result
    }

    private fun identifySpans(text: String): List<Node> {
        val result = mutableListOf<Node>()
        identifySpansInText(text, regexBold) { span: String, isInside: Boolean ->
            if (isInside) {
                // Currently, we support only bold or normal text
                result.add(SpanText(span, SpanText.Style.BOLD))
            } else {
                result.add(SpanText(span, SpanText.Style.NORMAL))
            }
        }
        return result
    }

    private fun parseLink(link: String): StructLink {
        val split = link.splitCsv()
        check(split.size == 2) { "Unknown parameters for a link: $link" }
        return StructLink(split[0], URL(split[1]))
    }
}