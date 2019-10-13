package com.blaster.data.managers.kotlin.visitors

import com.blaster.data.managers.kotlin.KotlinParser
import com.blaster.data.managers.kotlin.KotlinParserBaseVisitor

class StatementsVisitor(private val lambda: (KotlinParser.StatementsContext) -> Unit) :
    KotlinParserBaseVisitor<Unit>() {

    override fun visitStatements(ctx: KotlinParser.StatementsContext?) {
        lambda(ctx!!)
    }
}