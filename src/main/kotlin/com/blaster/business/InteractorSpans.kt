package com.blaster.business

import com.blaster.data.nodes.*

private val BOLD_REGEX = "'([^']+)'".toRegex()

class InteractorSpans {
    fun identifySpans(nodes: List<Node>): List<Node> {
        val result = mutableListOf<Node>()
        nodes.forEach { result.add(identifySpansInNodes(it)) }
        return result
    }

    private fun identifySpansInNodes(node: Node): Node {
        return when (node) {
            is NodeText -> {
                val transformed = mutableListOf<Node>()
                node.children.forEach { transformed.addAll(identifySpansInStructs(it)) }
                node.copy(children = transformed)
            }
            else -> node
        }
    }

    private fun identifySpansInStructs(struct: Node): List<Node> {
        return when (struct) {
            is StructText -> identifySpansInTextStruct(struct)
            is StructListItem -> {
                val transformed = mutableListOf<Node>()
                struct.children.forEach { transformed.addAll(identifySpansInStructs(it)) }
                listOf<Node>(struct.copy(children = transformed))
            }
            is StructLink -> listOf()
            is StructCite -> listOf()
            else -> TODO()
        }
    }

    private fun identifySpansInTextStruct(structText: StructText): List<Node> {
        val result = mutableListOf<Node>()
        identifySpansInText(structText.text, BOLD_REGEX) { span: String, isInside: Boolean ->
            if (isInside) {
                result.add(SpanText(span, SpanText.Style.BOLD))
            } else {
                result.add(SpanText(span, SpanText.Style.NORMAL))
            }
        }
        return result
    }
}