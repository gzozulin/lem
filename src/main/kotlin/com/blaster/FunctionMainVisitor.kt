package com.blaster

import KotlinParser
import KotlinParserBaseVisitor
import com.blaster.inserts.Insert
import org.antlr.v4.runtime.CommonTokenStream

class FunctionMainVisitor(private val tokenStream: CommonTokenStream) : KotlinParserBaseVisitor<List<Insert>>() {
    private val result = ArrayList<Insert>()

    override fun aggregateResult(aggregate: List<Insert>?, nextResult: List<Insert>?): List<Insert> {
        return result
    }

    override fun visitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext?): List<Insert> {
        if (ctx!!.identifier().text == "main") {
            result.addAll(FunctionBodyVisitor(tokenStream).visit(ctx.functionBody()))
        }
        return result
    }
}