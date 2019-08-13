package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCode
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.inserts.InsertText
import com.blaster.data.managers.printing.PrintingManager
import com.blaster.platform.LEM_COMPONENT
import java.lang.IllegalStateException
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
                    when (insert.type) {
                        InsertCommand.Type.INCLUDE -> {
                            // todo: render children for all inserts, not just for commands
                            result += printingManager.renderTemplate(
                                "template_children.ftlh",
                                hashMapOf("cmd" to insert.argument, "children" to printInserts(insert.children, true))
                            ) + "\n"
                        }
                        InsertCommand.Type.HEADER -> {
                            result += printingManager.renderTemplate(
                                "template_header.ftlh",
                                hashMapOf("type" to insert.subcommand, "header" to insert.argument)
                            ) + "\n"
                        }
                        else -> throw IllegalStateException("Unhandled command!")
                    }
                }
                is InsertText -> {
                    result += printingManager.renderTemplate(
                        "template_text.ftlh",
                        hashMapOf(textTemplateClass(child), "text" to insert.text)) + "\n"
                }
                is InsertCode -> {
                    result += printingManager.renderTemplate(
                        "template_code.ftlh",
                        hashMapOf(codeTemplateClass(child), "code" to insert.code)) + "\n"
                }
            }
        }
        return result.dropLast(1)
    }

    private fun codeTemplateClass(child: Boolean) = "class" to if (child) "code_child" else "code"
    private fun textTemplateClass(child: Boolean) = "class" to if (child) "text_child" else "text"
}