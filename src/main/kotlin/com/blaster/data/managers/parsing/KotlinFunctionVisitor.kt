package com.blaster.data.managers.parsing

class KotlinFunctionVisitor(private val identifier: String)
    : KotlinParserBaseVisitor<KotlinParser.FunctionBodyContext?>() {

    private var result: KotlinParser.FunctionBodyContext? = null

    override fun aggregateResult(
        aggregate: KotlinParser.FunctionBodyContext?,
        nextResult: KotlinParser.FunctionBodyContext?): KotlinParser.FunctionBodyContext?
    {
        if (result == null && nextResult != null) {
            result = nextResult
        }
        return result
    }

    override fun visitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext?): KotlinParser.FunctionBodyContext? {
        if (ctx!!.identifier().text != identifier) {
            return null
        }
        return ctx.functionBody()
    }
}