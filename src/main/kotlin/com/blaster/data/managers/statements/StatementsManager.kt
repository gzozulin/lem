package com.blaster.data.managers.statements

import com.blaster.data.paragraphs.Node

interface StatementsManager {
    fun extractStatements(code: String): List<Node>
}