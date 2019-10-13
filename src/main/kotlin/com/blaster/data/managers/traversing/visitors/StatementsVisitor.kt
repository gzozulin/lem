package com.blaster.data.managers.traversing.visitors

import com.blaster.data.managers.traversing.KotlinParser
import com.blaster.data.managers.traversing.KotlinParserBaseVisitor

class StatementsVisitor(private val lambda: (KotlinParser.StatementsContext) -> Unit) :
    KotlinParserBaseVisitor<Unit>() {

    override fun visitStatements(ctx: KotlinParser.StatementsContext?) {
        lambda(ctx!!)
    }
}