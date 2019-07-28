package com.blaster

import KotlinParser
import KotlinParserBaseVisitor
import com.blaster.inserts.Insert
import com.blaster.inserts.InsertCode
import com.blaster.inserts.InsertText
import org.antlr.v4.runtime.CommonTokenStream

class FunctionBodyVisitor(private val tokenStream: CommonTokenStream) : KotlinParserBaseVisitor<List<Insert>>() {
    private val result = ArrayList<Insert>()

    override fun aggregateResult(aggregate: List<Insert>?, nextResult: List<Insert>?): List<Insert> {
        return result
    }

    override fun visitStatement(ctx: KotlinParser.StatementContext?): List<Insert> {
        val comment = tokenStream.getHiddenTokensToLeft(ctx!!.start.tokenIndex - 2)
        if (comment != null) {
            result.add(InsertText(comment.toString()))
        }
        result.add(InsertCode(ctx.text))
        return result
    }
}