package com.blaster.data.managers.kotlin

import com.blaster.business.Location

interface KotlinManager {
    fun extractDefinition(location: Location): String
    fun extractDeclaration(location: Location): String
}