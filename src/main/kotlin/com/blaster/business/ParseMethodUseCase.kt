package com.blaster.business

import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.data.managers.storing.StoringManager
import com.blaster.platform.LEM_COMPONENT
import javax.inject.Inject

class ParseMethodUseCase {
    @Inject
    lateinit var locatorUseCase: LocatorUseCase

    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var storingManager: StoringManager

    @Inject
    lateinit var convertInsertsUseCase: ConvertInsertsUseCase

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseMethod(methodpath: String) {
        val located = locatorUseCase.locate(methodpath)
        val tokenStream = parsingManager.createTokenStream(located.file)
        val parser = parsingManager.createParser(tokenStream)
        val inserts = parsingManager.parseMethod(tokenStream, parser, located.clazz, located.member)
        val converted = convertInsertsUseCase.convertInserts(inserts)
        storingManager.storeInserts(located, converted)
    }
}