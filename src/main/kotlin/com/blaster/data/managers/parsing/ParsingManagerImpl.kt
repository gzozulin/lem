package com.blaster.data.managers.parsing

import org.antlr.v4.runtime.CharStreams
import java.io.File

import com.blaster.data.entities.Insert
import org.antlr.v4.runtime.CommonTokenStream

class ParsingManagerImpl : ParsingManager {
    override fun createTokenStream(file: File) = CommonTokenStream(KotlinLexer(CharStreams.fromFileName(file.absolutePath)))

    override fun createParser(tokenStream: CommonTokenStream) = KotlinParser(tokenStream)

    override fun parseMethodDef(
        tokenStream: CommonTokenStream, parser: KotlinParser,
        clazz: String?, method: String
    ): List<Insert> =
        if (clazz != null) {
            val classBody = KotlinClassVisitor(clazz).visitKotlinFile(parser.kotlinFile())
            val functionBody = KotlinFunctionVisitor(method).visitClassBody(classBody)
            FunctionBodyVisitor(tokenStream).visit(functionBody)
        } else {
            val functionBody = KotlinFunctionVisitor(method).visitKotlinFile(parser.kotlinFile())
            FunctionBodyVisitor(tokenStream).visit(functionBody)
        }
}