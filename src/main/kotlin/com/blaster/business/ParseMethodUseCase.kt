package com.blaster.business

import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.data.managers.storing.StoringManager
import com.blaster.platform.LEM_COMPONENT
import java.io.File
import javax.inject.Inject

class ParseMethodUseCase {
    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var storingManager: StoringManager

    @Inject
    lateinit var convertInsertsUseCase: ConvertInsertsUseCase

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseMethods(roots: List<File>) {
        roots.forEach { parseMethod(it) }
    }

    fun parseMethod(root: File) {
        val tokenStream = parsingManager.createTokenStream(root)
        val parser = parsingManager.createParser(tokenStream)
        val inserts = parsingManager.parseMain(tokenStream, parser)
        val converted = convertInsertsUseCase.convertInserts(inserts)
        storingManager.storeInserts(root, converted)
    }
}