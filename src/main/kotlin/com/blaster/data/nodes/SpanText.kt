package com.blaster.data.nodes

data class SpanText(val text: String, val style: Style) : Node() {
    enum class Style { NORMAL, BOLD }
}