package com.blaster.platform

import KotlinParserBaseListener
import com.blaster.data.Article
import com.blaster.data.Paragraph

class ClassListener(private val article: Article) : KotlinParserBaseListener() {
    /*override fun enterClassDeclaration(ctx: KotlinParser.ClassDeclarationContext?) {
        super.enterClassDeclaration(ctx)
        article.paragraphs.add(Paragraph("class"))
    }*/
}