package com.blaster.business

import com.blaster.data.nodes.*
import java.net.URL

private val LIST_ITEM_REGEX = "~\\s*(.+)\$".toRegex()
private val LINK_REGEX = "\\[([^\\[]+)\\]".toRegex()
private val CITE_REGEX = "\\\\([^\\\\]+)\\\\".toRegex()
private val BOLD_REGEX = "'([^']+)'".toRegex()

class InteractorStructs {
    fun identifyStructs(nodes: List<Node>): List<Node> {
        val result = mutableListOf<Node>()
        for (node in nodes) {
            result.add(when (node) {
                is NodeText -> {
                    val transformed = mutableListOf<Node>()
                    transformed.addAll(identifyListItems(node))
                    node.copy(children = transformed)
                }
                else -> node
            })
        }
        return result
    }

    private fun identifyListItems(node: NodeText): List<Node> {
        val match = LIST_ITEM_REGEX.find(node.text)
        return if (match != null) {
            val identified = identifyLinks(match.groups[1]!!.value)
            listOf(StructListItem(identified))
        } else {
            identifyLinks(node.text)
        }
    }

    private fun identifyLinks(text: String): List<Node> {
        val result = mutableListOf<Node>()
        identifySpansInText(text, LINK_REGEX) { span: String, isInside: Boolean ->
            result.addAll(if (isInside) {
                listOf(parseLink(span))
            } else {
                identifyCites(span)
            })
        }
        return result
    }

    private fun identifyCites(text: String): List<Node> {
        val result = mutableListOf<Node>()
        identifySpansInText(text, CITE_REGEX) { span: String, isInside: Boolean ->
            result.add(if (isInside) {
                StructCite(span)
            } else {
                val children = identifySpansInText(span)
                StructText(children)
            })
        }
        return result
    }

    private fun identifySpansInText(text: String): List<Node> {
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

    private fun parseLink(link: String): StructLink {
        val split = splitCsv(link)
        check(split.size == 2) { "Unknown parameters for a link: $link" }
        return StructLink(split[0], URL(split[1]))
    }
}