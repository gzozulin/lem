package com.blaster.business

import com.blaster.data.inserts.*
import com.blaster.data.managers.parsing.KotlinParser
import com.blaster.platform.LEM_COMPONENT
import org.antlr.v4.runtime.CommonTokenStream
import javax.inject.Inject

class InteractorStatements {
    @Inject
    lateinit var interactorTokens: InteractorTokens

    init {
        LEM_COMPONENT.inject(this)
    }

    fun extractStatements(tokenStream: CommonTokenStream, statements: KotlinParser.StatementsContext): List<Insert> {
        val tokens = tokenStream.getTokens(statements.start.tokenIndex + 1, statements.stop.tokenIndex - 1)
        return interactorTokens.extractStatements(tokens)
    }
}