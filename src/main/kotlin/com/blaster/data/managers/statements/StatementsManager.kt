package com.blaster.data.managers.statements

import com.blaster.data.inserts.Insert

interface StatementsManager {
    fun extractStatements(code: String): List<Insert>
}