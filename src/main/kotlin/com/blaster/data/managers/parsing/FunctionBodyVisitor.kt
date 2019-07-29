package com.blaster.data.managers.parsing

import com.blaster.data.entities.Insert
import com.blaster.data.entities.InsertCode
import com.blaster.data.entities.InsertText
import org.antlr.v4.runtime.CommonTokenStream


// todo combine comments, code
// todo will skip comments if line in between

class FunctionBodyVisitor(private val tokenStream: CommonTokenStream) : KotlinParserBaseVisitor<List<Insert>>() {
    private val result = ArrayList<Insert>()

    override fun aggregateResult(aggregate: List<Insert>?, nextResult: List<Insert>?): List<Insert> {
        return result
    }

    override fun visitStatement(ctx: KotlinParser.StatementContext?): List<Insert> {
        checkIfLeftIsComment(ctx!!.start.tokenIndex)
        result.add(InsertCode(ctx.text))
        return result
    }

    private fun checkIfLeftIsComment(currentIndex: Int) {
        val commentTokens = tokenStream.getHiddenTokensToLeft(currentIndex - 1)
        if (commentTokens != null) {
            val commentToken = commentTokens[0]
            checkIfLeftIsComment(commentToken.tokenIndex)
            result.add(InsertText(commentToken.text))
        }
    }
}