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

    private fun printInserts(inserts: List<Insert>): String {
        var result = ""
        for (insert in inserts) {
            when (insert) {
                is InsertCommand -> {
                    result += printingManager.renderTemplate(
                        "template_insert_children.ftlh", hashMapOf("children" to printInserts(insert.children))) + "\n"
                }
                is InsertText -> {
                    result += printingManager.renderTemplate(
                        "template_insert_text.ftlh", hashMapOf("text" to insert.text)) + "\n"
                }
                is InsertCode -> {
                    result += printingManager.renderTemplate(
                        "template_insert_code.ftlh", hashMapOf("code" to insert.code)) + "\n"
                }
            }
        }
        return result.dropLast(1)
    }
}