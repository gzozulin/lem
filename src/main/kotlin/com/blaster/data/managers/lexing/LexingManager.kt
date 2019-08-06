package com.blaster.data.managers.lexing

import com.blaster.data.managers.parsing.KotlinParser
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

interface LexingManager {
    fun provideParser(file: File): Pair<CommonTokenStream, KotlinParser>
}