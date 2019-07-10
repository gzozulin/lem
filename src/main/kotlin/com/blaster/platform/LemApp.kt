package com.blaster.platform

import KotlinLexer
import KotlinParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

/**
 * Hello, I am a comment
 */
class LemApp {}

fun main() {
    val characters = CharStreams.fromFileName("src/main/kotlin/com/blaster/platform/LemApp.kt")
    val lexer = KotlinLexer(characters)
    val tokens = CommonTokenStream(lexer)
    val parser = KotlinParser(tokens)
    val walker = ParseTreeWalker()
    val listener = LemListener()
    walker.walk(listener, parser.kotlinFile())
}