package com.blaster.business

private val CSV_PATTERN = ";\\s?".toPattern()

class InteractorCsv {
    fun splitCsv(text: String) = text.split(CSV_PATTERN)
}