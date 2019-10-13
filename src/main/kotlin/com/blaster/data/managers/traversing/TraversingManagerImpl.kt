package com.blaster.data.managers.traversing

import com.blaster.business.LocationClass
import com.blaster.business.LocationGlobal
import com.blaster.business.LocationMember
import com.blaster.data.managers.traversing.visitors.ClassDeclVisitor
import com.blaster.data.managers.traversing.visitors.GlobalDeclVisitor
import com.blaster.data.managers.traversing.visitors.MemberDeclVisitor
import com.blaster.data.managers.traversing.visitors.StatementsVisitor
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

class TraversingManagerImpl : TraversingManager {
    override fun locateGlobalMethodStatements(tokenStream: CommonTokenStream, parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.StatementsContext {
        // todo: can be triggered by member with same name if comes first
        var result: KotlinParser.StatementsContext? = null
        GlobalDeclVisitor(locationGlobal.identifier) { functionDecl ->
            StatementsVisitor { statements ->
                result = statements
            }.visit(functionDecl.functionBody())
        }.visitKotlinFile(parser.kotlinFile())
        checkNotNull(result) { "Nothing found for specified location $locationGlobal" }
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
        checkNotNull(result) { "Nothing found for specified location $locationMember" }
        return result!!
    }

    override fun locateGlobalMethodDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.FunctionDeclarationContext {
        // todo: can be triggered by member with same name if comes first
        var result: KotlinParser.FunctionDeclarationContext? = null
        GlobalDeclVisitor(locationGlobal.identifier) { functionDecl ->
            result = functionDecl
        }.visitKotlinFile(parser.kotlinFile())
        checkNotNull(result) { "Nothing found for specified location $locationGlobal" }
        return result!!
    }

    override fun locateMemberDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationMember: LocationMember): ParserRuleContext {
        var result: ParserRuleContext? = null
        ClassDeclVisitor(locationMember.clazz) { classDecl ->
            MemberDeclVisitor(locationMember.identifier) { memberDecl ->
                result = memberDecl
            }.visitClassBody(classDecl.classBody())
        }.visitKotlinFile(parser.kotlinFile())
        checkNotNull(result) { "Nothing found for specified location $locationMember" }
        return result!!
    }

    override fun locateClassDecls(tokenStream: CommonTokenStream, parser: KotlinParser, locationClass: LocationClass): List<ParserRuleContext> {
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

    override fun locateStatements(tokenStream: CommonTokenStream, parser: StatementsParser): List<ParserRuleContext> {
        val result = ArrayList<ParserRuleContext>()
        object : StatementsBaseVisitor<Unit>() {
            override fun visitLineComment(ctx: StatementsParser.LineCommentContext?) {
                result.add(ctx!!)
            }

            override fun visitDelimitedComment(ctx: StatementsParser.DelimitedCommentContext?) {
                result.add(ctx!!)
            }

            override fun visitCode(ctx: StatementsParser.CodeContext?) {
                result.add(ctx!!)
            }
        }.visitStatements(parser.statements())
        return result
    }
}