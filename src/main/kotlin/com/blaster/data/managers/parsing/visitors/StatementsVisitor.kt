package com.blaster.data.managers.parsing.visitors

import com.blaster.data.managers.parsing.KotlinParser
import com.blaster.data.managers.parsing.KotlinParserBaseVisitor

class StatementsVisitor(private val lambda: (KotlinParser.StatementsContext) -> Unit) : KotlinParserBaseVisitor<Unit>() {
    override fun visitStatements(ctx: KotlinParser.StatementsContext?) {
        lambda(ctx!!)
    }
}