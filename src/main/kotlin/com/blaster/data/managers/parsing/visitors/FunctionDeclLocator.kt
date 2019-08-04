package com.blaster.data.managers.parsing.visitors

import com.blaster.data.managers.parsing.KotlinParser
import com.blaster.data.managers.parsing.KotlinParserBaseVisitor

class FunctionDeclLocator(
    private val identifier: String,
    private val lambda: (KotlinParser.FunctionDeclarationContext) -> Unit) : KotlinParserBaseVisitor<Unit>()
{
    override fun visitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext?) {
        if (ctx!!.identifier().text == identifier) {
            lambda(ctx)
        }
    }
}