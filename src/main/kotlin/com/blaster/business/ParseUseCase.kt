package com.blaster.business

import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.data.managers.storing.StoringManager
import com.blaster.platform.LEM_COMPONENT
import java.io.File
import javax.inject.Inject

class ParseUseCase {
    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var storingManager: StoringManager

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseRoots(roots: List<File>) {
        roots.forEach { parseRoot(it) }
    }

    private fun parseRoot(root: File) {
        val tokenStream = parsingManager.createTokenStream(root)
        val parser = parsingManager.createParser(tokenStream)
        val inserts = parsingManager.parseMain(tokenStream, parser)
        storingManager.storeInserts(root, inserts)
    }
}