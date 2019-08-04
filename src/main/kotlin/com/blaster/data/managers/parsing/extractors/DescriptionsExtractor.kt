package com.blaster.data.managers.parsing.extractors

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertComment
import com.blaster.data.managers.parsing.KotlinParser
import org.antlr.v4.runtime.CommonTokenStream

class DescriptionsExtractor {
    fun extractDescriptions(tokenStream: CommonTokenStream, functionDecl: KotlinParser.FunctionDeclarationContext): List<Insert> {
        val hidden = tokenStream.getHiddenTokensToLeft(functionDecl.start.tokenIndex - 1) ?: return listOf()
        var code = ""
        val codeLines = codeLines(gatherTokens(tokenStream.get(functionDecl.start.tokenIndex, functionDecl.functionBody().start.tokenIndex - 1)))
        for (codeLine in codeLines) {
            code += codeLine + '\n'
        }
        code = code.dropLast(1)
        return listOf(InsertComment(hidden[0].text), InsertCode(code))
    }
}