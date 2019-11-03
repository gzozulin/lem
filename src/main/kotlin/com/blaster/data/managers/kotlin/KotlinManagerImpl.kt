package com.blaster.data.managers.kotlin

import com.blaster.business.Location
import com.blaster.business.LocationClass
import com.blaster.business.LocationGlobal
import com.blaster.business.LocationMember
import com.blaster.data.managers.kotlin.visitors.ClassDeclVisitor
import com.blaster.data.managers.kotlin.visitors.GlobalDeclVisitor
import com.blaster.data.managers.kotlin.visitors.MemberDeclVisitor
import io.reactivex.Observable
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.io.File

class KotlinManagerImpl : KotlinManager {
    private val parsers = HashMap<File, Pair<CommonTokenStream, KotlinParser>>()

    override fun extractDefinition(location: Location): String {
        val (tokenStream, parser) = provideParserForKotlin(location.file)
        parser.reset()
        val statements = when (location) {
            is LocationGlobal -> locateGlobalMethodStatements(parser, location)
            is LocationMember -> locateMemberMethodStatements(parser, location)
            else -> throw UnsupportedOperationException()
        }
        val tokens = tokenStream.getTokens(statements.start.tokenIndex, statements.stop.tokenIndex)
        return tokensToText(tokens)
    }

    override fun extractDeclaration(location: Location): List<String> {
        val (tokenStream, parser) = provideParserForKotlin(location.file)
        parser.reset()
        val declarations = when (location) {
            is LocationGlobal -> listOf(locateGlobalMethodDecl(parser, location))
            is LocationMember -> listOf(locateMemberDecl(parser, location))
            is LocationClass -> locateClassDecls(parser, location)
            else -> throw UnsupportedOperationException()
        }
        return Observable.fromIterable(declarations)
            .map { extractDeclaration(tokenStream, it) }
            .toList()
            .blockingGet()
    }

    private fun extractDeclaration(tokenStream: CommonTokenStream, memberDecl: ParserRuleContext): String {
        val lastToken = when (memberDecl) {
            is KotlinParser.ClassDeclarationContext -> {
                if (memberDecl.classBody() != null) {
                    tokenStream.get(memberDecl.classBody().start.tokenIndex - 1)
                } else {
                    tokenStream.get(memberDecl.stop.tokenIndex) // class have no body
                }
            }
            is KotlinParser.FunctionDeclarationContext -> {
                if (memberDecl.functionBody() != null) {
                    tokenStream.get(memberDecl.functionBody().start.tokenIndex - 1)
                } else {
                    tokenStream.get(memberDecl.stop.tokenIndex) // function have no body
                }
            }
            is KotlinParser.PropertyDeclarationContext -> memberDecl.stop
            else -> throw UnsupportedOperationException("Unknown type of member!")
        }
        val prevDecl = findPrevDeclaration(tokenStream, memberDecl.start.tokenIndex)
        val tokens = if (prevDecl != null) {
            tokenStream.get(prevDecl.tokenIndex + 1, lastToken.tokenIndex)
        } else {
            tokenStream.get(memberDecl.start.tokenIndex, lastToken.tokenIndex)
        }
        return tokensToText(tokens)
    }

    private fun findPrevDeclaration(tokenStream: CommonTokenStream, index: Int): Token? {
        var current = index - 1
        while(current >= 0) {
            val token = tokenStream.get(current)
            val text = token.text
            // not hidden, not blank, not new line
            if (token.channel != 1 && !text.isBlank()) {
                return token
            }
            current--
        }
        return null
    }

    private fun provideParserForKotlin(file: File): Pair<CommonTokenStream, KotlinParser> {
        var result = parsers[file]
        if (result == null) {
            val stream = CommonTokenStream(KotlinLexer(CharStreams.fromFileName(file.absolutePath)))
            val parser = KotlinParser(stream)
            result = stream to parser
            parsers[file] = result
        }
        return result
    }

    private fun tokensToText(tokens: List<Token>): String {
        var result = ""
        for (token in tokens) {
            result += token.text
        }
        return result
    }

    private fun locateGlobalMethodStatements(parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.FunctionBodyContext {
        // todo: can be triggered by member with same name if comes first
        var result: KotlinParser.FunctionBodyContext? = null
        GlobalDeclVisitor(locationGlobal.identifier) { functionDecl ->
            result = functionDecl.functionBody()
        }.visitKotlinFile(parser.kotlinFile())
        checkNotNull(result) { "Nothing found for specified location $locationGlobal" }
        return result!!
    }

    private fun locateMemberMethodStatements(parser: KotlinParser, locationMember: LocationMember): KotlinParser.FunctionBodyContext {
        var result: KotlinParser.FunctionBodyContext? = null
        ClassDeclVisitor(locationMember.clazz) { classDecl ->
            val globalDecl = GlobalDeclVisitor(locationMember.identifier) { functionDecl ->
                result = functionDecl.functionBody()
            }
            if (classDecl.classBody() != null) { // the class can have no body
                globalDecl.visitClassBody(classDecl.classBody())
            }
        }.visitKotlinFile(parser.kotlinFile())
        checkNotNull(result) { "Nothing found for specified location $locationMember" }
        return result!!
    }

    private fun locateGlobalMethodDecl(parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.FunctionDeclarationContext {
        // todo: can be triggered by member with same name if comes first
        var result: KotlinParser.FunctionDeclarationContext? = null
        GlobalDeclVisitor(locationGlobal.identifier) { functionDecl ->
            result = functionDecl
        }.visitKotlinFile(parser.kotlinFile())
        checkNotNull(result) { "Nothing found for specified location $locationGlobal" }
        return result!!
    }

    private fun locateMemberDecl(parser: KotlinParser, locationMember: LocationMember): ParserRuleContext {
        var result: ParserRuleContext? = null
        ClassDeclVisitor(locationMember.clazz) { classDecl ->
            MemberDeclVisitor(locationMember.identifier) { memberDecl ->
                result = memberDecl
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        checkNotNull(result) { "Nothing found for specified location $locationMember" }
        return result!!
    }

    private fun locateClassDecls(parser: KotlinParser, locationClass: LocationClass): List<ParserRuleContext> {
        val result = ArrayList<ParserRuleContext>()
        ClassDeclVisitor(locationClass.clazz) { classDecl ->
            result.add(classDecl)
            MemberDeclVisitor(null) { memberDecl ->
                result.add(memberDecl)
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        check(result.isNotEmpty()) { "Nothing found for specified location $locationClass" }
        return result
    }
}