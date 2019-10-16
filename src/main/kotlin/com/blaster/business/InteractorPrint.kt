package com.blaster.business

import com.blaster.data.inserts.*
import com.blaster.data.managers.printing.PrintingManager
import com.blaster.platform.LEM_COMPONENT
import java.io.File
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

    fun printArticle(sourceRoot: File, path: String, inserts: List<Insert>) {
        val located = interactorLocation.locate(sourceRoot, path)
        val article = printingManager.renderTemplate("template_article.ftlh", hashMapOf("article" to printInserts(inserts)))
        printingManager.printArticle(located.file, article)
    }

    private fun printInserts(inserts: List<Insert>, child: Boolean = false): String {
        var result = ""
        for (insert in inserts) {
            when (insert) {
                is InsertText -> result += renderText(insert.text, child) + "\n"
                is InsertCode -> result += renderCode(insert.code, child) + "\n"
                is InsertCommand -> {
                    when (insert.type) {
                        InsertCommand.Type.HEADER -> result += renderHeader(insert.subcommand, insert.argument) + "\n"
                        InsertCommand.Type.INCLUDE -> {
                            when (insert.subcommand) {
                                SUBCOMMAND_LINK -> result + renderLink(insert.argument, insert.argument1, insert.argument2, child) + "\n"
                                SUBCOMMAND_PICTURE -> result + renderPicture(insert.argument, insert.argument1, insert.argument2, child) + "\n"
                            }
                        }
                        else -> throw IllegalStateException("Unhandled command!")
                    }
                }
            }
            if (insert.children.isNotEmpty()) {
                result += renderChildren((insert as InsertCommand).argument, insert.children, true) + "\n"
            }
        }
        return result.dropLast(1)
    }

    private fun renderChildren(path: String, children: List<Insert>, child: Boolean): String {
        return printingManager.renderTemplate(
            "template_children.ftlh", hashMapOf("path" to path, "children" to printInserts(children, child)))
    }

    private fun renderText(text: String, child: Boolean): String {
        val clz = if (child) "text_child" else "text"
        return printingManager.renderTemplate("template_text.ftlh", hashMapOf("class" to clz, "text" to text))
    }

    private fun renderCode(code: String, child: Boolean): String {
        val clz = if (child) "code_child" else "code"
        return printingManager.renderTemplate("template_code.ftlh", hashMapOf("class" to clz, "code" to code))
    }

    private fun renderHeader(type: String, text: String): String {
        return printingManager.renderTemplate("template_header.ftlh", hashMapOf("type" to type, "header" to text))
    }

    private fun renderLink(label: String, descr: String, link: String, child: Boolean): String {
        val clz = if (child) "link_child" else "link"
        return printingManager.renderTemplate(
            "template_link.ftlh", hashMapOf("class" to clz, "label" to label, "descr" to descr, "link" to link))
    }

    private fun renderPicture(label: String, descr: String, link: String, child: Boolean): String {
        val clz = if (child) "picture_child" else "picture"
        return printingManager.renderTemplate(
            "template_picture.ftlh", hashMapOf("class" to clz, "label" to label, "descr" to descr, "link" to link))
    }
}