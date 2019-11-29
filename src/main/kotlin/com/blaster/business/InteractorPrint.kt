package com.blaster.business

import com.blaster.data.paragraphs.*
import com.blaster.data.managers.printing.PrintingManager
import com.blaster.platform.LEM_COMPONENT
import java.io.File
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

    // This call allows us to print the body of the article - a list of paragraphs. One thing to note is that this routine can be called recursively. The style of the output will look slightly differently. This fact is reflected by the additional parameter 'child'. The result of this method is the HTML generated.
    private fun printParagraphs(paragraphs: List<Paragraph>, child: Boolean = false): String {
        // We create the variable to hold the result and then we go through the paragraphs one by one
        var result = ""
        for (paragraph in paragraphs) {
            result += when (paragraph) {
                // For each type we call the appropriate routine
                is ParagraphText -> renderParagraphText(paragraph, child)
                is ParagraphCode -> renderParagraphCode(paragraph, child)
                is ParagraphCommand -> renderParagraphCommand(paragraph, child)
                else -> TODO()
            }
        }
        // The final result is returned from the call. It will always contain one unnecessary '\n' character, so we're cutting that out
        return result.dropLast(1)
    }

    private fun renderParagraphText(paragraph: ParagraphText, child: Boolean): String {
        var text = ""
        for (ch in paragraph.children) {
            text += when (ch) {
                is StructListItem -> printTemplateListItem(ch.item, child)
                else -> printTemplateText(paragraph.text, child)
            }
        }
        return printTemplateText(text, child) + "\n"
    }

    private fun renderParagraphCode(paragraph: ParagraphCode, child: Boolean): String {
        return printTemplateCode(paragraph.code, child) + "\n"
    }

    private fun renderParagraphCommand(paragraph: ParagraphCommand, child: Boolean): String {
        var result = ""
        when (paragraph.type) {
            // It can be something related to the attributes of the page
            ParagraphCommand.Type.HEADER -> result += printTemplateHeader(paragraph.subcommand, paragraph.argument) + "\n"
            // Or some insert - like a reference or a picture
            ParagraphCommand.Type.INCLUDE -> {
                when (paragraph.subcommand) {
                    SUBCOMMAND_LINK -> result + printTemplateLink(paragraph.argument, paragraph.argument1, paragraph.argument2, child) + "\n"
                    SUBCOMMAND_PICTURE -> result + printTemplatePicture(paragraph.argument, paragraph.argument1, paragraph.argument2, child) + "\n"
                }
            }
            else -> TODO()
        }
        if (paragraph.children.isNotEmpty()) {
            result += printTemplateChild(paragraph.argument, paragraph.children) + "\n"
        }
        return result
    }

    private fun printTemplateChild(path: String, children: List<Paragraph>): String {
        return printingManager.renderTemplate(
            "template_children.ftlh", hashMapOf("path" to path, "children" to printParagraphs(children, true)))
    }

    // Here is how we print text paragraph
    private fun printTemplateText(text: String, child: Boolean): String {
        // If this text is a child of another paragraph, appropriate style is selected
        val clz = if (child) "text_child" else "text"
        // Then we select a template and pass the task to the printing manager
        return printingManager.renderTemplate("template_text.ftlh", hashMapOf("class" to clz, "text" to text))
    }

    private fun printTemplateCode(code: String, child: Boolean): String {
        val clz = if (child) "code_child" else "code"
        return printingManager.renderTemplate("template_code.ftlh", hashMapOf("class" to clz, "code" to code))
    }

    private fun printTemplateHeader(type: String, text: String): String {
        return printingManager.renderTemplate("template_header.ftlh", hashMapOf("type" to type, "header" to text))
    }

    private fun printTemplateLink(label: String, descr: String, link: String, child: Boolean): String {
        val clz = if (child) "link_child" else "link"
        return printingManager.renderTemplate(
            "template_link.ftlh", hashMapOf("class" to clz, "label" to label, "descr" to descr, "link" to link))
    }

    private fun printTemplatePicture(label: String, descr: String, link: String, child: Boolean): String {
        val clz = if (child) "picture_child" else "picture"
        return printingManager.renderTemplate(
            "template_picture.ftlh", hashMapOf("class" to clz, "label" to label, "descr" to descr, "link" to link))
    }

    private fun printTemplateListItem(item: String, child: Boolean): String {
        val clz = if (child) "list_item_child" else "list_item"
        return printingManager.renderTemplate("template_list_item.ftlh", hashMapOf("class" to clz, "item" to item))
    }
}