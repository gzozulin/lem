package com.blaster.data.managers.printing

import java.io.File

interface PrintingManager {
    fun renderTemplate(id: String, dateModel: Any): String
    fun printArticle(file: File, article: String)
}