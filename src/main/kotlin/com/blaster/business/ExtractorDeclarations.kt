package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.managers.parsing.KotlinParser
import com.blaster.platform.LEM_COMPONENT
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import javax.inject.Inject

class ExtractorDeclarations {
    @Inject
    lateinit var extractorTokens: ExtractorTokens

    init {
        LEM_COMPONENT.inject(this)
    }

    fun extractDeclaration(tokenStream: CommonTokenStream, memberDecl: ParserRuleContext): List<Insert> {
        val lastToken = when (memberDecl) {
            is KotlinParser.ClassDeclarationContext    -> tokenStream.get(memberDecl.classBody().start.tokenIndex - 1)
            is KotlinParser.FunctionDeclarationContext -> tokenStream.get(memberDecl.functionBody().start.tokenIndex - 1)
            is KotlinParser.PropertyDeclarationContext -> memberDecl.stop
            else -> throw UnsupportedOperationException("Unknown type of member!")
        }
        val prevDecl = findPrevDeclaration(tokenStream, memberDecl.start.tokenIndex)
        val tokens = if (prevDecl != null) {
            tokenStream.get(prevDecl.tokenIndex + 1, lastToken.tokenIndex)
        } else {
            tokenStream.get(memberDecl.start.tokenIndex, lastToken.tokenIndex)
        }
        return extractorTokens.extractStatements(tokens)
    }

    private fun findPrevDeclaration(tokenStream: CommonTokenStream, index: Int): Token? {
        var current = index - 1
        while(current >= 0) {
            val token = tokenStream.get(current)
            val text = token.text
            // not hidden, not blank, not new line
            if (token.channel != 1 && !text.isBlank()) {
                return token
            }
            current--
        }
        return null
    }
}