package com.blaster.business

import com.blaster.data.entities.Insert
import com.blaster.data.managers.printing.PrintingManager
import com.blaster.data.managers.storing.StoringManager
import com.blaster.platform.LEM_COMPONENT
import java.io.File
import javax.inject.Inject

class PrintUseCase {
    @Inject
    lateinit var storingManager: StoringManager

    @Inject
    lateinit var printingManager: PrintingManager

    init {
        LEM_COMPONENT.inject(this)
    }

    fun printArticles() {
        val inserts = storingManager.getInserts()
        for (insert in inserts) {
            printArticle(insert.key, insert.value)
        }
    }

    private fun printArticle(root: File, inserts: List<Insert>) {
        printingManager.startArticleFor(root)
        for (insert in inserts) {
            printingManager.appendArticle(root, insert)
        }
        printingManager.finishArticleFor(root)
    }
}