package com.blaster.business

import com.blaster.data.paragraphs.Paragraph
import com.blaster.data.paragraphs.ParagraphText
import com.blaster.data.paragraphs.SpanListItem
import com.blaster.data.paragraphs.SpanText

private val LIST_ITEM_REGEX = "~\\s*(.+)\$".toRegex()

class InteractorStructs {
    fun identifyStructs(paragraphs: List<Paragraph>): List<Paragraph> {
        val result = mutableListOf<Paragraph>()
        for (paragraph in paragraphs) {
            if (paragraph is ParagraphText) {
                identifyStructsInText(paragraph)
            }
        }
        return result
    }

    private fun identifyStructsInText(paragraph: ParagraphText) {
        val match = LIST_ITEM_REGEX.find(paragraph.text)
        paragraph.children.add(
            if (match != null) {
                SpanListItem(match.groups[1]!!.value)
            } else {
                SpanText(paragraph.text)
            }
        )
    }
}