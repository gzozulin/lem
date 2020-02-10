package com.blaster.data.managers.printing

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler
import java.io.File
import java.io.FileOutputStream
import java.io.StringWriter

class PrintingManagerImpl : PrintingManager {
    private val configuration: Configuration = Configuration(Configuration.VERSION_2_3_27)

    init {
        configuration.setDirectoryForTemplateLoading(File("templates"))
        configuration.defaultEncoding = "UTF-8"
        configuration.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
        configuration.logTemplateExceptions = false
        configuration.wrapUncheckedExceptions = true
    }

    private val templateCache = HashMap<String, Template>()

    override fun renderTemplate(id: String, dateModel: Any): String {
        val template = getTemplate(id)
        val sw = StringWriter()
        template.process(dateModel, sw)
        return sw.toString()
    }

    override fun printArticle(file: File, article: String) {
        if (file.exists()) {
            file.delete()
        }
        articleWriter(file).use {
            it.write(article)
        }
    }

    private fun getTemplate(id: String): Template {
        var template = templateCache[id]
        if (template == null) {
            template = configuration.getTemplate(id)
            templateCache[id] = template
        }
        return template!!
    }

    private fun articleWriter(articleFile: File) = FileOutputStream(articleFile, true).bufferedWriter()
}