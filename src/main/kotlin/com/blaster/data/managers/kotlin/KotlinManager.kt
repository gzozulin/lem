package com.blaster.data.managers.kotlin

import com.blaster.business.Location
import com.blaster.business.LocationClass
import com.blaster.business.LocationGlobal
import com.blaster.business.LocationMember
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext

interface KotlinManager {
    fun extractDefinition(location: Location): String
    fun extractDeclaration(location: Location): List<String>
}