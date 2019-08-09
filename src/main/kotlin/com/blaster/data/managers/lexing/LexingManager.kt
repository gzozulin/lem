package com.blaster.data.managers.lexing

import com.blaster.data.managers.parsing.KotlinParser
import com.blaster.data.managers.parsing.StatementsParser
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

interface LexingManager {
    fun provideParserForKotlin(file: File): Pair<CommonTokenStream, KotlinParser>
    fun provideParserForStatememts(key: String): Pair<CommonTokenStream, StatementsParser>
}