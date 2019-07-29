package com.blaster.data.managers.printing

import com.blaster.data.entities.Insert
import java.io.File

interface PrintingManager {
    fun startArticleFor(root: File)
    fun finishArticleFor(root: File)
    fun appendArticle(root: File, insert: Insert)
}