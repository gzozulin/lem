package com.blaster.platform

import KotlinLexer
import KotlinParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class LemApp {}

fun main() {
    /**
     * The main target of lem is to provide direct sync between a code and an article generated.
     * main() is the official entry point to whole operation.
     * We start by slurping the characters from a file, then we follow up with creating a lexer.
     * The main goal of lexer is to split our characters stream into independent tokens.
     */
    val characters = CharStreams.fromFileName("src/main/kotlin/com/blaster/platform/LemApp.kt")
    val lexer = KotlinLexer(characters)
    val tokens = CommonTokenStream(lexer)

    /**
     * After the tokens are extracted, we can proceed by creating the parser and feeding our tokens into it.
     */
    val parser = KotlinParser(tokens)

    /**
     * #include [ClassVisitor]
     * With visitor on hands, we can perform the traversal:
     */
    val visitor = ClassVisitor()
    val result = visitor.visit(parser.kotlinFile()) // here should be main
    System.out.println(result)
}