package com.blaster.business

import com.blaster.data.nodes.*

private val LIST_ITEM_REGEX = "~\\s*(.+)\$".toRegex()
private val LINK_REGEX = "\\[([^\\[]+)\\]".toRegex()
private val CITE_REGEX = "\\\\([^\\\\]+)\\\\".toRegex()

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

    private fun identifyListItems(paragraph: NodeText): List<Node> {
        val match = LIST_ITEM_REGEX.find(paragraph.text)
        return if (match != null) {
            val identified = identifyLinks(match.groups[1]!!.value)
            listOf(StructListItem(identified))
        } else {
            identifyLinks(paragraph.text)
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
                StructText(span)
            })
        }
        return result
    }

    private fun parseLink(link: String): StructLink {
        val split = splitCsv(link)
        check(split.size == 2) { "Unknown parameters for a link: $link" }
        return StructLink(split[0], split[1])
    }
}