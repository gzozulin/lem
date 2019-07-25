package com.blaster

import KotlinParser
import KotlinParserBaseVisitor
import org.antlr.v4.runtime.CommonTokenStream

class FunctionBodyVisitor(private val tokenStream: CommonTokenStream) : KotlinParserBaseVisitor<List<Insert>>() {
    override fun visitStatements(ctx: KotlinParser.StatementsContext?): List<Insert> {
        val hidden = tokenStream.getHiddenTokensToRight(ctx!!.start.tokenIndex)
        return super.visitStatements(ctx)
    }
}