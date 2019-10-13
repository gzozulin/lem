package com.blaster.data.managers.traversing.visitors

import com.blaster.data.managers.traversing.KotlinParser
import com.blaster.data.managers.traversing.KotlinParserBaseVisitor

class GlobalDeclVisitor(
    private val identifier: String,
    private val lambda: (KotlinParser.FunctionDeclarationContext) -> Unit
) : KotlinParserBaseVisitor<Unit>() {

    // todo: we need listener for that
    /*private val classStack = Stack<String>()
    override fun visitClassDeclaration(ctx: KotlinParser.ClassDeclarationContext?) {
        classStack.push(ctx!!.simpleIdentifier().text)
        super.visitClassDeclaration(ctx)
    }*/

    override fun visitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext?) {
        if (/*classStack.isEmpty() && */ctx!!.identifier().text == identifier) {
            lambda(ctx)
        }
    }
}