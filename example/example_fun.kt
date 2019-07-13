package com.blaster.platform;

import KotlinLexer
import KotlinParser
import com.blaster.data.Article
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

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

    val article = Article(ArrayList())

    val listener = FunctionListener(article)
    ParseTreeWalker.DEFAULT.walk(listener, parser.kotlinFile())

    System.out.println(article)
}