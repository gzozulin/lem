package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertText
import com.blaster.data.managers.parsing.KotlinParser
import io.reactivex.Observable
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

class ExtractorDeclarations {
    fun extractDeclaration(tokenStream: CommonTokenStream, memberDecl: ParserRuleContext): List<Insert> {
        return listOf(
            findCommentToLeft(tokenStream, memberDecl.start.tokenIndex),
            collectDeclaration(tokenStream, memberDecl)
        )
    }

    private fun findCommentToLeft(tokenStream: CommonTokenStream, index: Int): Insert {
        val current = index - 1
        val hidden = tokenStream.getHiddenTokensToLeft(current)
            ?: return findCommentToLeft(tokenStream, current)
        for (token in hidden) {
            if (!token.text.isBlank()) {
                check((token.text.contains("/*") && token.text.contains("*/")) || token.text.contains("//")) { "We have a very strange comment" }
                return commentToText(token.text)
            }
        }
        return findCommentToLeft(tokenStream, hidden.last().tokenIndex)
    }

    private fun collectDeclaration(tokenStream: CommonTokenStream, memberDecl: ParserRuleContext): InsertCode {
        val lastToken = when (memberDecl) {
            is KotlinParser.FunctionDeclarationContext -> memberDecl.functionBody().start
            is KotlinParser.PropertyDeclarationContext -> memberDecl.stop
            else -> throw UnsupportedOperationException("Unknown type of member!")
        }
        return Observable.just(tokenStream.get(memberDecl.start.tokenIndex, lastToken.tokenIndex))
            .map { tokensToText(it) }
            .map { textToLines(it) }
            .map { trimCommonSpaces(it) }
            .map { linesToText(it) }
            .map { InsertCode(it) }
            .blockingFirst()

    }
}