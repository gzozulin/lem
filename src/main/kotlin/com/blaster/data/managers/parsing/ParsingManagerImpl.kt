package com.blaster.data.managers.parsing

import com.blaster.business.LocationClass
import com.blaster.business.LocationGlobal
import com.blaster.business.LocationMember

import com.blaster.data.managers.parsing.visitors.ClassDeclLocator
import com.blaster.data.managers.parsing.visitors.FunctionDeclLocator
import com.blaster.data.managers.parsing.visitors.FunctionStatementsLocator
import com.blaster.data.managers.parsing.visitors.MemberDeclLocator
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

class ParsingManagerImpl : ParsingManager {
    override fun locateGlobalMethodStatements(tokenStream: CommonTokenStream, parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.StatementsContext {
        // todo: can be triggered by member with same name if comes first
        var result: KotlinParser.StatementsContext? = null
        FunctionDeclLocator(locationGlobal.identifier) { functionDecl ->
            FunctionStatementsLocator { statements ->
                result = statements
            }.visit(functionDecl.functionBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun locateMemberMethodStatements(tokenStream: CommonTokenStream, parser: KotlinParser, locationMember: LocationMember): KotlinParser.StatementsContext {
        var result: KotlinParser.StatementsContext? = null
        ClassDeclLocator(locationMember.clazz) { classDecl ->
            FunctionDeclLocator(locationMember.identifier) { functionDecl ->
                FunctionStatementsLocator { statements ->
                    result = statements
                }.visit(functionDecl.functionBody())
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun locateGlobalMethodDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.FunctionDeclarationContext {
        // todo: can be triggered by member with same name if comes first
        var result: KotlinParser.FunctionDeclarationContext? = null
        FunctionDeclLocator(locationGlobal.identifier) { functionDecl ->
            result = functionDecl
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun locateMemberDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationMember: LocationMember): ParserRuleContext {
        var result: ParserRuleContext? = null
        ClassDeclLocator(locationMember.clazz) { classDecl ->
            MemberDeclLocator(locationMember.identifier) { memberDecl ->
                result = memberDecl
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun locateClassDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationClass: LocationClass): List<ParserRuleContext> {
        val result = ArrayList<ParserRuleContext>()
        ClassDeclLocator(locationClass.clazz) { classDecl ->
            MemberDeclLocator(null) { memberDecl ->
                result.add(memberDecl)
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result
    }
}