package com.blaster.data.managers.parsing

import com.blaster.business.ClassLocation
import com.blaster.business.GlobalLocation
import com.blaster.business.MemberLocation
import org.antlr.v4.runtime.CharStreams
import java.io.File

import com.blaster.data.inserts.Insert
import com.blaster.data.managers.parsing.extractors.DescriptionsExtractor
import com.blaster.data.managers.parsing.extractors.StatementsExtractor
import com.blaster.data.managers.parsing.visitors.ClassDeclLocator
import com.blaster.data.managers.parsing.visitors.FunctionDeclLocator
import com.blaster.data.managers.parsing.visitors.FunctionStatementsLocator
import org.antlr.v4.runtime.CommonTokenStream

class ParsingManagerImpl : ParsingManager {
    override fun parseGlobalMethodDef(location: GlobalLocation): List<Insert> {
        val tokenStream = createTokenStream(location.file)
        val parser = createParser(tokenStream)
        // todo: can be triggered by member with same name if comes first
        var result: List<Insert>? = null
        FunctionDeclLocator(location.identifier) { functionDecl ->
            FunctionStatementsLocator { statements ->
                result = StatementsExtractor().extractStatements(tokenStream, statements)
            }.visit(functionDecl.functionBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun parseMemberMethodDef(location: MemberLocation): List<Insert> {
        val tokenStream = createTokenStream(location.file)
        val parser = createParser(tokenStream)
        var result: List<Insert>? = null
        ClassDeclLocator(location.clazz) { classDecl ->
            FunctionDeclLocator(location.identifier) { functionDecl ->
                FunctionStatementsLocator { statements ->
                    result = StatementsExtractor().extractStatements(tokenStream, statements)
                }.visit(functionDecl.functionBody())
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun parseGlobalDecl(location: GlobalLocation): List<Insert> {
        val tokenStream = createTokenStream(location.file)
        val parser = createParser(tokenStream)
        // todo: can be triggered by member with same name if comes first
        var result: List<Insert>? = null
        FunctionDeclLocator(location.identifier) { functionDecl ->
            result = DescriptionsExtractor().extractDescriptions(tokenStream, functionDecl)
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun parseMemberDecl(location: MemberLocation): List<Insert> {
        val tokenStream = createTokenStream(location.file)
        val parser = createParser(tokenStream)
        var result: List<Insert>? = null
        ClassDeclLocator(location.clazz) { classDecl ->
            FunctionDeclLocator(location.identifier) { functionDecl ->
                result = DescriptionsExtractor().extractDescriptions(tokenStream, functionDecl)
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun parseClassDecl(location: ClassLocation): List<Insert> {
        val tokenStream = createTokenStream(location.file)
        val parser = createParser(tokenStream)
        val result = ArrayList<Insert>()
        ClassDeclLocator(location.clazz) { classDecl ->
            // todo: result.addAll(properties)
            FunctionDeclLocator(location.clazz) { functionDecl ->
                result.addAll(DescriptionsExtractor().extractDescriptions(tokenStream, functionDecl))
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result
    }

    private fun createTokenStream(file: File) = CommonTokenStream(KotlinLexer(CharStreams.fromFileName(file.absolutePath)))

    private fun createParser(tokenStream: CommonTokenStream) = KotlinParser(tokenStream)
}