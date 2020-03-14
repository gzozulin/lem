package com.blaster.data.managers.kotlin

import com.blaster.business.Location
import com.blaster.data.managers.ParserCache
import org.antlr.v4.runtime.*
import java.io.File
import java.net.URL


typealias ClassContext = KotlinParser.ClassDeclarationContext
typealias FunctionContext = KotlinParser.FunctionDeclarationContext
typealias PropertyContext = KotlinParser.PropertyDeclarationContext

// todo: do normally
val accumulatedErrors = mutableMapOf<File, HashSet<String>>()

private class AccumulatingErrorListener(val file: File): BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?
    ) {
        synchronized(accumulatedErrors) {
            if (!accumulatedErrors.containsKey(file)) {
                accumulatedErrors[file] = HashSet()
            }
            accumulatedErrors[file]!!.add("line $line:$charPositionInLine $msg")
        }
    }
}

class KotlinManagerImpl : KotlinManager {
    private val parserCache = object : ParserCache<File, KotlinParser>() {
        override fun createParser(key: File): KotlinParser {
            val lexer = KotlinLexer(CharStreams.fromFileName(key.absolutePath))
            lexer.removeErrorListeners()
            val parser = KotlinParser(CommonTokenStream(lexer))
            parser.removeErrorListeners()
            parser.addErrorListener(AccumulatingErrorListener(key))
            return parser
        }
    }

    override fun extractDefinition(location: Location): String {
        var result = ""
        parserCache.useParser(location.file) { parser, stream ->
            val definition = locateCode(parser, location)
            location.url = URL(location.url.toString() + "#L${definition.start.line}") // todo: ugly shortcut
            val firstToken = findFirstToken(stream, definition)
            val lastToken = definition.stop.tokenIndex
            val tokens = stream.get(firstToken, lastToken)
            result = tokensToText(tokens)
        }
        return result
    }

    override fun extractDeclaration(location: Location): String {
        var result = ""
        parserCache.useParser(location.file) { parser, stream ->
            val declaration = locateCode(parser, location)
            location.url = URL(location.url.toString() + "#L${declaration.start.line}") // todo: ugly shortcut
            val firstToken = findFirstToken(stream, declaration)
            val lastToken = findLastToken(stream, declaration)
            val tokens = stream.get(firstToken, lastToken)
            result = tokensToText(tokens)
        }
        return result
    }

    private fun locateCode(parser: KotlinParser, location: Location): ParserRuleContext {
        var lastDeclaration: ParserRuleContext? = null
        val declarations = mutableListOf<ParserRuleContext>()
        val visitor = object : KotlinParserBaseVisitor<Unit>() {
            override fun visitClassDeclaration(ctx: ClassContext?) {
                super.visitClassDeclaration(ctx)
                if (ctx!!.simpleIdentifier().text == location.identifier) {
                    declarations.add(ctx)
                }
                lastDeclaration = ctx
            }

            override fun visitFunctionDeclaration(ctx: FunctionContext?) {
                super.visitFunctionDeclaration(ctx)
                if (ctx!!.identifier().text == location.identifier) {
                    declarations.add(ctx)
                }
                lastDeclaration = ctx
            }

            override fun visitPropertyDeclaration(ctx: PropertyContext?) {
                super.visitPropertyDeclaration(ctx)
                val variableDeclaration = ctx!!.variableDeclaration() ?: return
                if (variableDeclaration.simpleIdentifier().text == location.identifier) {
                    declarations.add(ctx)
                }
                lastDeclaration = ctx
            }
        }
        try {
            visitor.visitKotlinFile(parser.kotlinFile())
        } catch (err: AssertionError) {

        }
        if (declarations.isEmpty()) {
            throw AssertionError("Location not found: $location\n" +
                    "File was not fully parsed?! Last declaration: ${lastDeclaration?.text}")
        }
        return declarations.first()
    }

    private fun findFirstToken(tokenStream: CommonTokenStream, context: ParserRuleContext): Int {
        val prevDecl = findPrevDeclaration(tokenStream, context)
        return if (prevDecl != null) {
            prevDecl.tokenIndex + 1
        } else {
            context.start.tokenIndex
        }
    }

    private fun findLastToken(tokenStream: CommonTokenStream, decl: ParserRuleContext): Int {
        return when (decl) {
            is ClassContext -> {
                if (decl.classBody() != null) {
                    decl.classBody().start.tokenIndex - 1
                } else {
                    decl.stop.tokenIndex // class have no body
                }
            }
            is FunctionContext -> {
                if (decl.functionBody() != null) {
                    decl.functionBody().start.tokenIndex - 1
                } else {
                    decl.stop.tokenIndex // function have no body
                }
            }
            is PropertyContext -> {
                if (decl.text.contains("object")) {
                    findBodyStart(tokenStream, decl).tokenIndex - 2 // for objects before body begins
                } else {
                    decl.stop.tokenIndex // full declaration
                }
            }
            else -> throw UnsupportedOperationException("Unknown type of member!")
        }
    }

    // todo: sloppy and innacurate
    private fun findBodyStart(tokenStream: CommonTokenStream, context: ParserRuleContext): Token {
        val (from, to) = context.start.tokenIndex to context.stop.tokenIndex
        var current = from + 1
        while (current < to) {
            val token = tokenStream.get(current)
            if (token.text == "{") {
                return token
            }
            current++
        }
        throw IllegalStateException("Body of this object is not found! ${context.text}")
    }

    // todo: sloppy and innacurate
    private fun findPrevDeclaration(tokenStream: CommonTokenStream, member: ParserRuleContext): Token? {
        var current = member.start.tokenIndex - 1
        while(current >= 0) {
            val token = tokenStream.get(current)
            // not hidden, not blank, not new line
            if (token.channel != 1 && !token.text.isBlank()) {
                return token
            }
            current--
        }
        return null
    }

    private fun tokensToText(tokens: List<Token>): String {
        var result = ""
        for (token in tokens) {
            result += token.text
        }
        return result
    }
}