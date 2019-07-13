package com.blaster.platform

import KotlinParserBaseListener
import com.blaster.data.Article
import com.blaster.data.Paragraph

class FunctionListener(private val article: Article) : KotlinParserBaseListener() {
    /*override fun enterStatements(ctx: KotlinParser.StatementsContext?) {
        super.enterStatements(ctx)
        for (child in ctx!!.children) {
            if (!child.text.isBlank()) {
                article.paragraphs.add(Paragraph(child.text))
            }
        }
    }*/
}