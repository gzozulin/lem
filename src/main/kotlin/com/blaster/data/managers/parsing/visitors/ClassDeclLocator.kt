package com.blaster.data.managers.parsing.visitors

import com.blaster.data.managers.parsing.KotlinParser
import com.blaster.data.managers.parsing.KotlinParserBaseVisitor

class ClassDeclLocator(
    private val clazz: String,
    private val lambda: (KotlinParser.ClassDeclarationContext) -> Unit) : KotlinParserBaseVisitor<Unit>()
{
    override fun visitClassDeclaration(ctx: KotlinParser.ClassDeclarationContext?) {
        if (clazz.endsWith(ctx!!.simpleIdentifier().text)) {
            lambda(ctx)
        }
    }
}