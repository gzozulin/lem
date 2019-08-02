package com.blaster.data.managers.parsing

class KotlinClassVisitor(
    private val clazz: String
) : KotlinParserBaseVisitor<KotlinParser.ClassBodyContext?>() {

    private var result: KotlinParser.ClassBodyContext? = null

    override fun aggregateResult(
        aggregate: KotlinParser.ClassBodyContext?,
        nextResult: KotlinParser.ClassBodyContext?): KotlinParser.ClassBodyContext?
    {
        if (result == null && nextResult != null) {
            result = nextResult
        }
        return result
    }

    override fun visitClassDeclaration(ctx: KotlinParser.ClassDeclarationContext?): KotlinParser.ClassBodyContext? {
        if (ctx!!.simpleIdentifier().text != clazz) {
            return null
        }
        return ctx.classBody()
    }
}