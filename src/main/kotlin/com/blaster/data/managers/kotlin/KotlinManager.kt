package com.blaster.data.managers.kotlin

import com.blaster.business.Location
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

interface KotlinManager {
    fun extractDefinition(location: Location): String
    fun extractDeclaration(location: Location): List<String>
}