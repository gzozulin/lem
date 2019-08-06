package com.blaster.data.managers.parsing.visitors

import com.blaster.data.managers.parsing.KotlinParser
import com.blaster.data.managers.parsing.KotlinParserBaseVisitor
import org.antlr.v4.runtime.ParserRuleContext

class MemberDeclVisitor(private val identifier: String?, private val lambda: (ParserRuleContext) -> Unit) : KotlinParserBaseVisitor<Unit>() {
    override fun visitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext?) {
        if (identifier == null) {
            lambda(ctx!!)
        } else {
            if (ctx!!.identifier().text == identifier) {
                lambda(ctx)
            }
        }
    }

    override fun visitPropertyDeclaration(ctx: KotlinParser.PropertyDeclarationContext?) {
        check(ctx!!.multiVariableDeclaration() == null) { "We cannot work with multivar decls yet!" }
        if (identifier == null) {
            lambda(ctx)
        } else {
            if (ctx.variableDeclaration().simpleIdentifier().text == identifier) {
                lambda(ctx)
            }
        }
    }
}