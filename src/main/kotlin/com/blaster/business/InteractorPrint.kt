package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.inserts.InsertText
import com.blaster.data.managers.printing.PrintingManager
import com.blaster.platform.LEM_COMPONENT
import javax.inject.Inject

class InteractorPrint {
    @Inject
    lateinit var interactorLocation: InteractorLocation

    @Inject
    lateinit var printingManager: PrintingManager

    init {
        LEM_COMPONENT.inject(this)
    }

    fun printArticle(path: String, parsed: List<Insert>) {
        val located = interactorLocation.locate(path)
        val article = printingManager.renderTemplate("template_article.ftlh", hashMapOf("article" to printInserts(parsed)))
        printingManager.printArticle(located.file, article)
    }

    private fun printInserts(inserts: List<Insert>, child: Boolean = false): String {
        var result = ""
        for (insert in inserts) {
            when (insert) {
                is InsertCommand -> {
                    result += printingManager.renderTemplate(
                        "template_insert_children.ftlh",
                        hashMapOf("cmd" to insert.argument, "children" to printInserts(insert.children, true))
                    ) + "\n"
                }
                is InsertText -> {
                    result += printingManager.renderTemplate(
                        "template_insert_text.ftlh",
                        hashMapOf(textTemplateClass(child), "text" to insert.text)) + "\n"
                }
                is InsertCode -> {
                    result += printingManager.renderTemplate(
                        "template_insert_code.ftlh",
                        hashMapOf(codeTemplateClass(child), "code" to insert.code)) + "\n"
                }
            }
        }
        return result.dropLast(1)
    }

    private fun codeTemplateClass(child: Boolean) = "class" to if (child) "code_child" else "code"
    private fun textTemplateClass(child: Boolean) = "class" to if (child) "text_child" else "text"
}