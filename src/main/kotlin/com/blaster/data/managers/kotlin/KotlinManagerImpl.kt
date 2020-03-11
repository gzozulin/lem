package com.blaster.data.managers.kotlin

import com.blaster.business.Location
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.antlr.v4.runtime.*

import java.io.File
import java.net.URL

typealias ClassContext = KotlinParser.ClassDeclarationContext
typealias FunctionContext = KotlinParser.FunctionDeclarationContext
typealias PropertyContext = KotlinParser.PropertyDeclarationContext

private data class CacheEntry(var parser: KotlinParser? = null, val mutex: Mutex = Mutex())

class KotlinManagerImpl : KotlinManager {
    private val cache = mutableMapOf<File, CacheEntry>()
    private fun useParser(file: File, action: (parser: KotlinParser, stream: CommonTokenStream) -> String): String {
        var entry: CacheEntry
        synchronized(cache) {
           if (!cache.containsKey(file)) {
               cache[file] = CacheEntry()
           }
            entry = cache[file]!!
        }
        return runBlocking {
            entry.mutex.withLock {
                if (entry.parser == null) {
                    entry.parser = KotlinParser(CommonTokenStream(KotlinLexer(CharStreams.fromFileName(file.absolutePath))))
                }
                val parser = entry.parser!!
                val stream = parser.tokenStream as CommonTokenStream
                parser.reset()
                return@runBlocking action.invoke(parser, stream)
            }
        }
    }

    override fun extractDefinition(location: Location): String {
        return useParser(location.file) { parser, stream ->
            val definition = locateCode(parser, location)
            location.url = URL(location.url.toString() + "#L${definition.start.line}") // todo: ugly shortcut
            val firstToken = findFirstToken(stream, definition)
            val lastToken = definition.stop.tokenIndex
            val tokens = stream.get(firstToken, lastToken)
            return@useParser tokensToText(tokens)
        }
    }

    override fun extractDeclaration(location: Location): String {
        return useParser(location.file) { parser, stream ->
            val declaration = locateCode(parser, location)
            location.url = URL(location.url.toString() + "#L${declaration.start.line}") // todo: ugly shortcut
            val firstToken = findFirstToken(stream, declaration)
            val lastToken = findLastToken(stream, declaration)
            val tokens = stream.get(firstToken, lastToken)
            return@useParser tokensToText(tokens)
        }
    }

    private fun locateCode(parser: KotlinParser, location: Location): ParserRuleContext {
        val declarations = mutableListOf<ParserRuleContext>()
        val visitor = object : KotlinParserBaseVisitor<Unit>() {
            override fun visitClassDeclaration(ctx: ClassContext?) {
                super.visitClassDeclaration(ctx)
                if (ctx!!.simpleIdentifier().text == location.identifier) {
                    declarations.add(ctx)
                }
            }

            override fun visitFunctionDeclaration(ctx: FunctionContext?) {
                super.visitFunctionDeclaration(ctx)
                if (ctx!!.identifier().text == location.identifier) {
                    declarations.add(ctx)
                }
            }

            override fun visitPropertyDeclaration(ctx: PropertyContext?) {
                super.visitPropertyDeclaration(ctx)
                val variableDeclaration = ctx!!.variableDeclaration() ?: return
                if (variableDeclaration.simpleIdentifier().text == location.identifier) {
                    declarations.add(ctx)
                }
            }
        }
        visitor.visitKotlinFile(parser.kotlinFile())
        check(declarations.size != 0) { "Location not found: $location" }
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