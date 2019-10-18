package com.blaster.data.managers.printing

import com.blaster.platform.LEM_COMPONENT
import freemarker.template.Configuration
import freemarker.template.Template
import java.io.File
import java.io.FileOutputStream
import java.io.StringWriter
import javax.inject.Inject

class PrintingManagerImpl : PrintingManager {
    @Inject
    lateinit var configuration: Configuration

    private val templateCache = HashMap<String, Template>()

    init {
        LEM_COMPONENT.inject(this)
    }

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