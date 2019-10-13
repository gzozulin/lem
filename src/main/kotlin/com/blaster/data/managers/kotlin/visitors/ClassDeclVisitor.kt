package com.blaster.data.managers.kotlin.visitors

import com.blaster.data.managers.kotlin.KotlinParser
import com.blaster.data.managers.kotlin.KotlinParserBaseVisitor

class ClassDeclVisitor(
    private val clazz: String,
    private val lambda: (KotlinParser.ClassDeclarationContext) -> Unit
) : KotlinParserBaseVisitor<Unit>() {

    override fun visitClassDeclaration(ctx: KotlinParser.ClassDeclarationContext?) {
        if (clazz.endsWith(ctx!!.simpleIdentifier().text)) {
            lambda(ctx)
        }
    }
}