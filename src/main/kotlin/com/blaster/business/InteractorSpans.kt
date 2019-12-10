package com.blaster.business

import com.blaster.data.paragraphs.*

private val BOLD_REGEX = "'([^']+)'".toRegex()

class InteractorSpans {
    fun identifySpans(nodes: List<Node>): List<Node> {
        val result = mutableListOf<Node>()
        for (paragraph in nodes) {
            result.add(paragraph)
            if (paragraph is NodeText) {
                for (struct in paragraph.children) {
                    when (struct) {
                        is StructText -> identifyBoldSpansInText(struct)
                        is StructListItem -> {
                            for (child in struct.children) {
                                when (child) {
                                    is StructText -> identifyBoldSpansInText(child)
                                    is StructLink -> {} // nothing
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

    private fun identifyBoldSpansInText(structText: StructText) {
        identifySpansInText(structText.text, BOLD_REGEX) { span: String, isInside: Boolean ->
            structText.children.add(if (isInside) {
                SpanText(span, SpanText.Style.BOLD)
            } else {
                SpanText(span, SpanText.Style.NORMAL)
            })
        }
    }
}