package com.blaster.data.managers.parsing

import com.blaster.data.managers.kotlin.KotlinLexer
import com.blaster.data.managers.kotlin.KotlinParser
import com.blaster.data.managers.kotlin.StatementsLexer
import com.blaster.data.managers.kotlin.StatementsParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

class ParsingManagerImpl : ParsingManager {
    private val cacheForKotlin = HashMap<File, Pair<CommonTokenStream, KotlinParser>>()
    private val cacheForStatements = HashMap<String, Pair<CommonTokenStream, StatementsParser>>()

    override fun provideParserForKotlin(file: File): Pair<CommonTokenStream, KotlinParser> {
        var result = cacheForKotlin[file]
        if (result == null) {
            val stream = CommonTokenStream(KotlinLexer(CharStreams.fromFileName(file.absolutePath)))
            val parser = KotlinParser(stream)
            result = stream to parser
            cacheForKotlin[file] = result
        }
        return result
    }

    override fun provideParserForStatememts(key: String): Pair<CommonTokenStream, StatementsParser> {
        var result = cacheForStatements[key]
        if (result == null) {
            val stream = CommonTokenStream(StatementsLexer(CharStreams.fromString(key)))
            val parser = StatementsParser(stream)
            result = stream to parser
            cacheForStatements[key] = result
        }
        return result
    }
}