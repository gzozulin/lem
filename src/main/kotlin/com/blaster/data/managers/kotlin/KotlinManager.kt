package com.blaster.data.managers.kotlin

import com.blaster.business.Location
import com.blaster.business.LocationClass
import com.blaster.business.LocationGlobal
import com.blaster.business.LocationMember
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

interface KotlinManager {
    fun extractDef(location: Location): String
    fun extractDecl(location: Location): List<String>

    // todo: should be refactored to its own parser
    fun locateStatements(tokenStream: CommonTokenStream, parser: StatementsParser): List<ParserRuleContext>
}