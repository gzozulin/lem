package com.blaster.data.managers.parsing

import com.blaster.business.LocationClass
import com.blaster.business.LocationGlobal
import com.blaster.business.LocationMember
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

interface ParsingManager {
    fun locateGlobalMethodStatements(tokenStream: CommonTokenStream, parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.StatementsContext
    fun locateMemberMethodStatements(tokenStream: CommonTokenStream, parser: KotlinParser, locationMember: LocationMember): KotlinParser.StatementsContext
    fun locateGlobalMethodDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationGlobal: LocationGlobal): KotlinParser.FunctionDeclarationContext
    fun locateMemberDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationMember: LocationMember): ParserRuleContext
    fun locateClassDecl(tokenStream: CommonTokenStream, parser: KotlinParser, locationClass: LocationClass): List<ParserRuleContext>
    fun locateStatements(tokenStream: CommonTokenStream, parser: StatementsParser): List<ParserRuleContext>
}