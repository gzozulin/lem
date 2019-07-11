package com.blaster.platform

import KotlinParserBaseVisitor

/**
 * Visitor allows us to navigate the abstract tree in a nice and convenient way.
 */
class ClassVisitor : KotlinParserBaseVisitor<String>() {
    override fun visitClassDeclaration(ctx: KotlinParser.ClassDeclarationContext?): String {
        return ctx!!.start.text
    }

    override fun aggregateResult(aggregate: String?, nextResult: String?): String {
        return super.aggregateResult(aggregate, nextResult)
    }
}