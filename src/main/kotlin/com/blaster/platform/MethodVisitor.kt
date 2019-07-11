package com.blaster.platform

import KotlinParserBaseVisitor

/**
 * Visitor allows us to navigate the abstract tree in a nice and convenient way.
 */
class MethodVisitor : KotlinParserBaseVisitor<String>() {

    /**
     * It contains only one method: visitCommentSection()
     */
    override fun visitCommentSection(ctx: KotlinParser.CommentSectionContext?): String {
        return ctx!!.DelimitedComment().text
    }
}