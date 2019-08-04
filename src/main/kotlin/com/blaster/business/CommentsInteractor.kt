package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertText

class CommentsInteractor {
    private val singleLineRegex = "^//(.*)\$".toRegex()
    private val delimitedRegex = "^/\\*((.*\\n?)+)\\*/\$".toRegex()

    fun processComment(comment: String): Insert {
        val singleLineMatch = singleLineRegex.find(comment)
        if (singleLineMatch != null) {
            return InsertText(singleLineMatch.groups[1]!!.value)
        }
        val delimitedMatch = delimitedRegex.find(comment)
        if (delimitedMatch != null) {
            var cleaned = delimitedMatch.groups[1]!!.value
            cleaned = cleaned.drop(1) // first '\n'
            cleaned = cleaned.dropLast(1) // trailing '\n'
            return InsertText(cleaned)
        }
        throw IllegalStateException("At least one of them should match!")
    }
}