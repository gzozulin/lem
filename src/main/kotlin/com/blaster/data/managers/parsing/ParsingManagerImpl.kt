package com.blaster.data.managers.parsing

import org.antlr.v4.runtime.CharStreams
import java.io.File

import com.blaster.data.entities.Insert
import org.antlr.v4.runtime.CommonTokenStream

class ParsingManagerImpl : ParsingManager {
    override fun createTokenStream(file: File) = CommonTokenStream(KotlinLexer(CharStreams.fromFileName(file.absolutePath)))

    override fun createParser(tokenStream: CommonTokenStream) = KotlinParser(tokenStream)

    override fun parseMethod(tokenStream: CommonTokenStream, parser: KotlinParser, clazz: String?, method: String): List<Insert> {
        if (clazz != null) {
            // locate in class
            throw NotImplementedError()
        } else {
            return GlobalMethodVisitor(tokenStream, method).visitKotlinFile(parser.kotlinFile())
        }
    }
}