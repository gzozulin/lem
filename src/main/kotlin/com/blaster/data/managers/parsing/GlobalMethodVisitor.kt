package com.blaster.data.managers.parsing

import com.blaster.data.entities.Insert
import org.antlr.v4.runtime.CommonTokenStream

class GlobalMethodVisitor(
    private val tokenStream: CommonTokenStream,
    val method: String
) : KotlinParserBaseVisitor<List<Insert>>()
{
    private val result = ArrayList<Insert>()

    override fun aggregateResult(aggregate: List<Insert>?, nextResult: List<Insert>?): List<Insert> {
        return result
    }

    override fun visitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext?): List<Insert> {
        if (ctx!!.identifier().text == method) {
            result.addAll(FunctionBodyVisitor(tokenStream).visit(ctx.functionBody()))
        }
        return result
    }
}