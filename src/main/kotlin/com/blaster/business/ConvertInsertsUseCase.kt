package com.blaster.business

import com.blaster.data.entities.Insert
import com.blaster.data.entities.InsertCommand
import com.blaster.data.entities.InsertComment
import com.blaster.platform.LEM_COMPONENT
import javax.inject.Inject

class ConvertInsertsUseCase {
    @Inject
    lateinit var parseCommandUseCase: ParseCommandUseCase

    @Inject
    lateinit var parseCommentUseCase: ParseCommentUseCase

    init {
        LEM_COMPONENT.inject(this)
    }

    fun convertInserts(inserts: List<Insert>) : List<Insert> {
        val result = ArrayList<Insert>()
        for (insert in inserts) {
            when (insert) {
                is InsertCommand -> {
                    result.addAll(parseCommandUseCase.parseCommand(insert.command))
                }
                is InsertComment -> {
                    result.add(parseCommentUseCase.parseComment(insert.comment))
                }
                else -> result.add(insert)
            }
        }
        return result
    }
}