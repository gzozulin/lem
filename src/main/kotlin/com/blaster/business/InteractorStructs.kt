package com.blaster.business

import com.blaster.data.paragraphs.Paragraph
import com.blaster.data.paragraphs.ParagraphText
import com.blaster.data.paragraphs.StructListItem
import com.blaster.data.paragraphs.StructText

private val LIST_ITEM_REGEX = "~\\s*(.+)\$".toRegex()

class InteractorStructs {
    fun identifyStructs(paragraphs: List<Paragraph>): List<Paragraph> {
        val result = mutableListOf<Paragraph>()
        for (paragraph in paragraphs) {
            result.add(paragraph)
            if (paragraph is ParagraphText) {
                paragraph.children.add(identifyStructsInText(paragraph))
            }
        }
        return result
    }

    private fun identifyStructsInText(paragraph: ParagraphText): Paragraph {
        val match = LIST_ITEM_REGEX.find(paragraph.text)
        return if (match != null) {
            StructListItem(match.groups[1]!!.value)
        } else {
            StructText(paragraph.text)
        }
    }
}