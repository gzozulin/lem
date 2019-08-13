package com.blaster.business

import com.blaster.data.inserts.*
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
                            val includeResult = renderIncludeCommand(insert, child)
                            if (includeResult != null) {
                                result += includeResult
                            }
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
            if (insert.children.isNotEmpty()) {
                // todo: not only for commands?
                result += printingManager.renderTemplate(
                    "template_children.ftlh",
                    hashMapOf("path" to (insert as InsertCommand).argument, "children" to printInserts(insert.children, true))
                ) + "\n"
            }
        }
        return result.dropLast(1)
    }

    private fun renderIncludeCommand(command: InsertCommand, child: Boolean): String? {
        when (command.subcommand) {
            SUBCOMMAND_LINK -> {
                val clazz = if (child) "link_child" else "link"
                return printingManager.renderTemplate(
                    "template_link.ftlh",
                    hashMapOf("class" to clazz, "label" to command.argument, "descr" to command.argument1, "link" to command.argument2)
                )
            }
            SUBCOMMAND_PICTURE -> {
                val clazz = if (child) "picture_child" else "picture"
                return printingManager.renderTemplate(
                    "template_picture.ftlh",
                    hashMapOf("class" to clazz, "label" to command.argument, "descr" to command.argument1, "link" to command.argument2)
                )
            }
            else -> {
                return null
            }
        }
    }

    private fun codeTemplateClass(child: Boolean) = "class" to if (child) "code_child" else "code"
    private fun textTemplateClass(child: Boolean) = "class" to if (child) "text_child" else "text"
}