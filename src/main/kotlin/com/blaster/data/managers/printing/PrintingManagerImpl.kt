package com.blaster.data.managers.printing

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertText
import com.blaster.platform.LEM_COMPONENT
import freemarker.template.Configuration
import freemarker.template.Template
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Named

const val HTML_START = """
<html>
    <head>
        <title>Welcome!</title>
    </head>
    <body> 
"""

const val HTML_END = """
    </body>
</html>
"""

class PrintingManagerImpl : PrintingManager {
    @Inject
    @field:Named("ARTICLES_FILE")
    lateinit var articlesDir: File

    @Inject
    lateinit var configuration: Configuration

    init {
        LEM_COMPONENT.inject(this)
    }

    private val textTemplate: Template
    private val codeTemplate: Template

    init {
        textTemplate = configuration.getTemplate("insert_text.ftlh")
        codeTemplate = configuration.getTemplate("insert_code.ftlh")
    }

    override fun startArticleFor(root: File) {
        val articleFile = articleFile(root)
        if (articleFile.exists()) {
            articleFile.delete()
        }
        articleWriter(articleFile).use {
            it.write(HTML_START)
        }
    }

    override fun appendArticle(root: File, insert: Insert) {
        articleWriter(articleFile(root)).use {
            when (insert) {
                is InsertText -> textTemplate.process(hashMapOf("text" to insert.text), it)
                is InsertCode -> codeTemplate.process(hashMapOf("code" to insert.code), it)
            }
            it.write("\n")
        }
    }

    override fun finishArticleFor(root: File) {
        articleWriter(articleFile(root)).use {
            it.write(HTML_END)
        }
    }

    private fun articleFile(root: File) = File(articlesDir.absoluteFile, root.nameWithoutExtension + ".html")

    private fun articleWriter(articleFile: File) = FileOutputStream(articleFile, true).bufferedWriter()
}