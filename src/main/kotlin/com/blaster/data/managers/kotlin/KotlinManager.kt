package com.blaster.data.managers.kotlin

import com.blaster.business.Location
import java.io.File

data class ReportedError(val line: Int, val char: Int, val msg: String)

interface KotlinManager {
    fun extractDefinition(location: Location): String
    fun extractDeclaration(location: Location): String
    fun reportErrors() : Map<File, Set<ReportedError>>
}