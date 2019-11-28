package com.blaster.data.paragraphs

data class SpanText(val text: String, val style: Style) : Paragraph() {
    enum class Style { NORMAL, BOLD }
}