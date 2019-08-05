package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertComment
import com.blaster.data.managers.parsing.KotlinParser
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.lang.UnsupportedOperationException

class ExtractorDeclarations {
    fun extractDeclarations(tokenStream: CommonTokenStream, memberDecl: ParserRuleContext): List<Insert> {
        return listOf(findCommentToLeft(tokenStream, memberDecl.start.tokenIndex), collectDeclaration(tokenStream, memberDecl))
    }

    private fun collectDeclaration(tokenStream: CommonTokenStream, memberDecl: ParserRuleContext): InsertCode {
        val lastToken = when (memberDecl) {
            is KotlinParser.FunctionDeclarationContext -> memberDecl.functionBody().start
            is KotlinParser.PropertyDeclarationContext -> memberDecl.stop
            else -> throw UnsupportedOperationException("Unknown type of member!")
        }
        var code = ""
        val codeLines = codeLines(
            gatherTokens(tokenStream.get(memberDecl.start.tokenIndex, lastToken.tokenIndex))
        )
        for (codeLine in codeLines) {
            code += codeLine + '\n'
        }
        code = code.dropLast(1)
        return InsertCode(code)
    }

    private fun findCommentToLeft(tokenStream: CommonTokenStream, index: Int): Insert {
        val current = index - 1
        val hidden = tokenStream.getHiddenTokensToLeft(current)
            ?: return findCommentToLeft(tokenStream, current)
        for (token in hidden) {
            if (!token.text.isBlank()) {
                check((token.text.contains("/*") && token.text.contains("*/")) || token.text.contains("//")) { "We have a very strange comment" }
                return collectComment(token)
            }
        }
        return findCommentToLeft(tokenStream, hidden.last().tokenIndex)
    }

    private fun collectComment(token: Token): Insert {
        return InsertComment(token.text)
    }
}