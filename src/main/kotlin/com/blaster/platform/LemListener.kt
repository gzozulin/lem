package com.blaster.platform

import KotlinParserBaseListener

class LemListener : KotlinParserBaseListener() {
    override fun enterCommentSection(ctx: KotlinParser.CommentSectionContext?) {
        super.enterCommentSection(ctx)
        System.out.println(ctx)
    }
}