package com.blaster.data.managers.lexing

import com.blaster.data.managers.parsing.KotlinLexer
import com.blaster.data.managers.parsing.KotlinParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

class LexingManagerImpl : LexingManager {
    private val cache = HashMap<File, Pair<CommonTokenStream, KotlinParser>>()

    override fun provideParser(file: File): Pair<CommonTokenStream, KotlinParser> {
        var result = cache[file]
        if (result == null) {
            val stream = createTokenStream(file)
            val parser = createParser(stream)
            result = stream to parser
            cache[file] = result
        }
        return result
    }

    private fun createTokenStream(file: File) = CommonTokenStream(KotlinLexer(CharStreams.fromFileName(file.absolutePath)))

    private fun createParser(tokenStream: CommonTokenStream) = KotlinParser(tokenStream)
}