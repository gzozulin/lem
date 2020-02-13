package com.blaster.business

import com.blaster.data.managers.printing.PrintingManager
import com.blaster.data.nodes.*
import com.blaster.data.nodes.SpanText.Style
import com.blaster.platform.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.net.URL

private val nonAlphaNumeric = """\W""".toRegex()
private fun String.headerId() = this.replace(nonAlphaNumeric, "-")

const val contentPlaceholder = "###ContentTableGoesHere###"

class InteractorPrint {
    private val printingManager: PrintingManager by kodein.instance()

    // Parameters of this function are: the source root, the output file and a list of nodes to be printed
    fun printArticle(output: File, nodes: List<Node>) {
        // After receiving a list of nodes, we wrap them into an article template
        val article = printingManager.renderTemplate(
            "template_article.ftlh", hashMapOf("article" to printParagraphs(nodes))
        )
        // The result is sent to printing manager to be put into a file
        printingManager.printArticle(output, article)
    }

    // This call allows us to print the body of the article - a list of nodes. One thing to note is that this routine can be called recursively. The style of the output will look slightly differently. This fact is reflected by the additional parameter 'child'. The result of this method is the HTML generated.
    private fun printParagraphs(nodes: List<Node>, child: Boolean = false): String {
        // We create the variable to hold the result and then we go through the nodes one by one
        var result = ""
        // We also want to keep track of headers and references, which we accumulated while rendering, we will place them before and after the article
        var contentHeader = "Contents"
        val headers = mutableListOf<NodeCommand>()
        val cites = mutableListOf<NodeCommand>()
        for (node in nodes) {
            result += when (node) {
                // For each type we call the appropriate template
                is NodeText -> renderNodeText(node, child)
                is NodeCode -> renderNodeCode(node, child)
                is NodeCommand -> {
                    if (node.cmdType == CmdType.CONTENT) {
                        contentHeader = node.subcommand
                    }
                    renderNodeCommand(node, child, headers, cites)
                }
                else -> TODO()
            }
        }
        // Now we can replace content placeholder with the actual table of contents
        if (headers.isNotEmpty()) {
            var contents = printTemplateHeader(contentHeader)
            headers.forEach {
                contents += printTemplateListItem(
                    printTemplateLink(it.subcommand, "#${it.subcommand.headerId()}", child = false, newWindow = false), false)
            }
            result = if (result.contains(contentPlaceholder)) {
                result.replace(contentPlaceholder, contents)
            } else {
                contents + result
            }
        }
        // After all of the nodes are rendered, we can add our references
        if (cites.isNotEmpty()) {
            result += printTemplateHeader("References")
            cites.forEach { node ->
                result += printTemplateListItem(
                    printTemplateLink(
                        "&#8593;[${node.subcommand}]: ", "#${node.subcommand}_origin", child, false) +
                            printTemplateCite(node.subcommand, node.argument, node.argument1, child), child)
            }
        }
        // The final result is returned from the call
        return result
    }

    // This routine will print a text node
    private fun renderNodeText(node: NodeText, child: Boolean): String {
        var result = ""
        for (ch in node.children) {
            result += when (ch) {
                // Depending on which children we have to render, we can select an appropriate template
                is StructListItem -> renderListItem(ch, child)
                is StructLink -> printTemplateLink(ch.text, ch.link.toString(), child)
                is StructCite -> printTemplateCiteLink(ch.id)
                is StructText -> renderTextSpans(ch.children)
                else -> TODO()
            }
        }
        // And then we can wrap it into a paragraph tags
        return printTemplateParagraph(result + "\n", child)
    }

    private fun renderListItem(listItem: StructListItem, child: Boolean): String {
        var result = ""
        for (ch in listItem.children) {
            result += when (ch) {
                is StructLink -> printTemplateLink(ch.text, ch.link.toString(), child)
                is StructCite -> printTemplateCiteLink(ch.id)
                is StructText -> renderTextSpans(ch.children)
                else -> TODO()
            }
        }
        return printTemplateListItem(result + "\n", child)
    }

    // A relatively straightforward routine to render the code. We just pass the code into a template
    private fun renderNodeCode(node: NodeCode, child: Boolean): String {
        return printTemplateCode(node.code, child) + "\n"
    }

    // With this routine we can include additions like headers, pictures and etc.
    private fun renderNodeCommand(node: NodeCommand, child: Boolean,
                                  headers: MutableList<NodeCommand>, cites: MutableList<NodeCommand>): String {
        var result = ""
        when (node.cmdType) {
            // It can be something related to the attributes of the page
            CmdType.HEADER -> {
                headers.add(node)
                result += printTemplateHeader(node.subcommand) + "\n"
            }
            // Or a picture insert
            CmdType.PICTURE -> result += printTemplatePicture(node.subcommand, node.argument, child) + "\n"
            // Or a cite reference
            CmdType.CITE -> cites.add(node)
            // Content placeholder
            CmdType.CONTENT -> result += contentPlaceholder
            // Else just continue
            else -> {}
        }
        // When we include the code and comments into the article they come as children of the command which included them
        if (node.children.isNotEmpty()) {
            result += printTemplateChild(node.argument, node.location!!.url, node.children) + "\n"
        }
        return result
    }

    private fun renderTextSpans(spans: List<Node>): String {
        var result = ""
        for (span in spans) {
            result += printTemplateSpan(span as SpanText)
        }
        return result
    }

    private fun printTemplateChild(path: String, url: URL, children: List<Node>): String {
        val pathLink = printTemplateLink(path, url.toString(), true)
        return printingManager.renderTemplate(
            "template_children.ftlh",
            hashMapOf("path" to pathLink, "children" to printParagraphs(children, true))
        )
    }

    // Here is how we print paragraph
    private fun printTemplateParagraph(paragraph: String, child: Boolean): String {
        // If this paragraph is a child of another paragraph, appropriate style is selected
        val clz = if (child) "text_child" else "text"
        // Then we select a template and pass the task to the printing manager
        return printingManager.renderTemplate("template_paragraph.ftlh", hashMapOf("class" to clz, "paragraph" to paragraph))
    }

    private fun printTemplateCode(code: String, child: Boolean): String {
        val clz = if (child) "code_child" else "code"
        return printingManager.renderTemplate("template_code.ftlh", hashMapOf("class" to clz, "code" to code))
    }

    private fun printTemplateHeader(text: String): String {
        return printingManager.renderTemplate("template_header.ftlh", hashMapOf("id" to text.headerId(), "header" to text))
    }

    private fun printTemplateLink(label: String, link: String, child: Boolean, newWindow: Boolean = true): String {
        val clz = if (child) "link_child" else "link"
        return printingManager.renderTemplate(
            "template_link.ftlh", hashMapOf("class" to clz, "label" to label, "link" to link, "newWindow" to newWindow))
    }

    private fun printTemplateCite(id: String, label: String, link: String, child: Boolean): String {
        val clz = if (child) "link_child" else "link"
        return printingManager.renderTemplate(
            "template_cite.ftlh", hashMapOf("id" to id, "class" to clz, "label" to label, "link" to link))
    }

    private fun printTemplateCiteLink(id: String): String {
        return printingManager.renderTemplate("template_cite_link.ftlh", hashMapOf("id" to id))
    }

    private fun printTemplatePicture(label: String, link: String, child: Boolean): String {
        val clz = if (child) "picture_child" else "picture"
        return printingManager.renderTemplate(
            "template_picture.ftlh", hashMapOf("class" to clz, "label" to label, "link" to link))
    }

    private fun printTemplateListItem(item: String, child: Boolean): String {
        val clz = if (child) "list_item_child" else "list_item"
        return printingManager.renderTemplate("template_list_item.ftlh", hashMapOf("class" to clz, "item" to item))
    }

    private fun printTemplateSpan(span: SpanText): String {
        return when {
            span.style == Style.BOLD -> printingManager.renderTemplate("template_span_bold.ftlh", hashMapOf("span" to span.text))
            else -> span.text
        }
    }
}