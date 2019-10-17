package com.blaster.data.managers.statements

import com.blaster.data.paragraphs.Paragraph

interface StatementsManager {
    fun extractStatements(code: String): List<Paragraph>
}