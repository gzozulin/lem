package com.blaster.data.managers.parsing

import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

import com.blaster.data.entities.Insert

interface ParsingManager {
    fun createTokenStream(file: File): CommonTokenStream
    fun createParser(tokenStream: CommonTokenStream): KotlinParser
    fun parseMethodDef(tokenStream: CommonTokenStream, parser: KotlinParser, clazz: String?, method: String) : List<Insert>
}