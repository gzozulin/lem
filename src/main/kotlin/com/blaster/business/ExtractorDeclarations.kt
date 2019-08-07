package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.managers.parsing.KotlinParser
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

class ExtractorDeclarations {
    fun extractDeclaration(tokenStream: CommonTokenStream, memberDecl: ParserRuleContext): List<Insert> {
        val lastToken = when (memberDecl) {
            is KotlinParser.FunctionDeclarationContext -> memberDecl.functionBody().start
            is KotlinParser.PropertyDeclarationContext -> memberDecl.stop
            else -> throw UnsupportedOperationException("Unknown type of member!")
        }
        val prevDecl = findPrevDeclaration(tokenStream, memberDecl.start.tokenIndex)
        val tokens = if (prevDecl != null) {
            tokenStream.get(prevDecl.tokenIndex + 1, lastToken.tokenIndex)
        } else {
            tokenStream.get(memberDecl.start.tokenIndex, lastToken.tokenIndex)
        }
        return ExtractorTokens(tokens).extractStatements()
    }

    private fun findPrevDeclaration(tokenStream: CommonTokenStream, index: Int): Token? {
        var current = index - 1
        while(current >= 0) {
            val token = tokenStream.get(current)
            val text = token.text
            // not hidden, not blank, not new line
            if (token.channel != 1 && !text.isBlank() && !isNewLine(text)) {
                return token
            }
            current--
        }
        return null
    }

    private fun isNewLine(text: String): Boolean {
        return text == "\n" || text == "\r\n"
    }
}