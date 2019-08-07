package com.blaster.business

import com.blaster.data.inserts.*
import com.blaster.data.managers.parsing.KotlinParser
import org.antlr.v4.runtime.CommonTokenStream

class ExtractorStatements {
    fun extractStatements(tokenStream: CommonTokenStream, statements: KotlinParser.StatementsContext): List<Insert> {
        val tokens = tokenStream.getTokens(statements.start.tokenIndex + 1, statements.stop.tokenIndex - 1)
        return ExtractorTokens(tokens).extractStatements()
    }
}