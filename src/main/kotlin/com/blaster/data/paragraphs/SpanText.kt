package com.blaster.data.paragraphs

data class SpanText(val text: String, val style: Style) : Node() {
    enum class Style { NORMAL, BOLD }
}