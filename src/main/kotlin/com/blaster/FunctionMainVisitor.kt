package com.blaster

import KotlinParser
import KotlinParserBaseVisitor
import org.antlr.v4.runtime.CommonTokenStream

class FunctionMainVisitor(private val tokenStream: CommonTokenStream) : KotlinParserBaseVisitor<List<Insert>>() {
    override fun visitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext?): List<Insert> {
        if (ctx!!.identifier().text == "main") {
            return FunctionBodyVisitor(tokenStream).visit(ctx.functionBody())
        }
        return emptyList()
    }
}