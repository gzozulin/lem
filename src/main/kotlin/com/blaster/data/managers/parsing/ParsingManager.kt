package com.blaster.data.managers.parsing

import com.blaster.data.managers.kotlin.KotlinParser
import com.blaster.data.managers.kotlin.StatementsParser
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

interface ParsingManager {
    fun provideParserForKotlin(file: File): Pair<CommonTokenStream, KotlinParser>
    fun provideParserForStatememts(key: String): Pair<CommonTokenStream, StatementsParser>
}