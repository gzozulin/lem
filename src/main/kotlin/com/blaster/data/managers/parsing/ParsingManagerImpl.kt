package com.blaster.data.managers.parsing

import com.blaster.business.LocationClass
import com.blaster.business.LocationGlobal
import com.blaster.business.LocationMember

import com.blaster.data.managers.parsing.visitors.ClassDeclVisitor
import com.blaster.data.managers.parsing.visitors.GlobalDeclVisitor
import com.blaster.data.managers.parsing.visitors.StatementsVisitor
import com.blaster.data.managers.parsing.visitors.MemberDeclVisitor
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

class ParsingManagerImpl : ParsingManager {
    override fun locateGlobalMethodStatements(tokenStream: CommonTokenStream, parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.StatementsContext {
        // todo: can be triggered by member with same name if comes first
        var result: KotlinParser.StatementsContext? = null
        GlobalDeclVisitor(locationGlobal.identifier) { functionDecl ->
            StatementsVisitor { statements ->
                result = statements
            }.visit(functionDecl.functionBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun locateMemberMethodStatements(tokenStream: CommonTokenStream, parser: KotlinParser, locationMember: LocationMember): KotlinParser.StatementsContext {
        var result: KotlinParser.StatementsContext? = null
        ClassDeclVisitor(locationMember.clazz) { classDecl ->
            GlobalDeclVisitor(locationMember.identifier) { functionDecl ->
                StatementsVisitor { statements ->
                    result = statements
                }.visit(functionDecl.functionBody())
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun locateGlobalMethodDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.FunctionDeclarationContext {
        // todo: can be triggered by member with same name if comes first
        var result: KotlinParser.FunctionDeclarationContext? = null
        GlobalDeclVisitor(locationGlobal.identifier) { functionDecl ->
            result = functionDecl
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun locateMemberDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationMember: LocationMember): ParserRuleContext {
        var result: ParserRuleContext? = null
        ClassDeclVisitor(locationMember.clazz) { classDecl ->
            MemberDeclVisitor(locationMember.identifier) { memberDecl ->
                result = memberDecl
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result!!
    }

    override fun locateClassDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationClass: LocationClass): List<ParserRuleContext> {
        val result = ArrayList<ParserRuleContext>()
        ClassDeclVisitor(locationClass.clazz) { classDecl ->
            MemberDeclVisitor(null) { memberDecl ->
                result.add(memberDecl)
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        return result
    }
}