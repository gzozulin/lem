package com.blaster.data.managers.statements

import com.blaster.data.nodes.Node

interface StatementsManager {
    fun extractStatements(code: String): List<Node>
}