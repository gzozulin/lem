package com.blaster.business

import com.blaster.data.paragraphs.*
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

    // Parameters of this function are: the output file and a list of paragraphs to be printed
    fun printArticle(output: File, paragraphs: List<Paragraph>) {
        // After receiving a list of paragraphs, we wrap them into an article template
        val article = printingManager.renderTemplate("template_article.ftlh", hashMapOf("article" to printParagraphs(paragraphs)))
        // The result is sent to printing manager to be put into a file
        printingManager.printArticle(output, article)
    }

    private fun printParagraphs(paragraphs: List<Paragraph>, child: Boolean = false): String {
        var result = ""
        for (insert in paragraphs) {
            when (insert) {
                is ParagraphText -> result += printText(insert.text, child) + "\n"
                is ParagraphCode -> result += printCode(insert.code, child) + "\n"
                is ParagraphCommand -> {
                    when (insert.type) {
                        ParagraphCommand.Type.HEADER -> result += printHeader(insert.subcommand, insert.argument) + "\n"
                        ParagraphCommand.Type.INCLUDE -> {
                            when (insert.subcommand) {
                                SUBCOMMAND_LINK -> result + printLink(insert.argument, insert.argument1, insert.argument2, child) + "\n"
                                SUBCOMMAND_PICTURE -> result + printPicture(insert.argument, insert.argument1, insert.argument2, child) + "\n"
                            }
                        }
                        else -> throw IllegalStateException("Unhandled command!")
                    }
                }
            }
            if (insert.children.isNotEmpty()) {
                result += printChild((insert as ParagraphCommand).argument, insert.children) + "\n"
            }
        }
        return result.dropLast(1)
    }

    private fun printChild(path: String, children: List<Paragraph>): String {
        return printingManager.renderTemplate(
            "template_children.ftlh", hashMapOf("path" to path, "children" to printParagraphs(children, true)))
    }

    private fun printText(text: String, child: Boolean): String {
        val clz = if (child) "text_child" else "text"
        return printingManager.renderTemplate("template_text.ftlh", hashMapOf("class" to clz, "text" to text))
    }

    private fun printCode(code: String, child: Boolean): String {
        val clz = if (child) "code_child" else "code"
        return printingManager.renderTemplate("template_code.ftlh", hashMapOf("class" to clz, "code" to code))
    }

    private fun printHeader(type: String, text: String): String {
        return printingManager.renderTemplate("template_header.ftlh", hashMapOf("type" to type, "header" to text))
    }

    private fun printLink(label: String, descr: String, link: String, child: Boolean): String {
        val clz = if (child) "link_child" else "link"
        return printingManager.renderTemplate(
            "template_link.ftlh", hashMapOf("class" to clz, "label" to label, "descr" to descr, "link" to link))
    }

    private fun printPicture(label: String, descr: String, link: String, child: Boolean): String {
        val clz = if (child) "picture_child" else "picture"
        return printingManager.renderTemplate(
            "template_picture.ftlh", hashMapOf("class" to clz, "label" to label, "descr" to descr, "link" to link))
    }
}