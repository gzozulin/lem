package com.blaster.business

import com.blaster.data.entities.Insert
import com.blaster.data.entities.InsertCommand
import com.blaster.data.entities.InsertComment
import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.platform.LEM_COMPONENT
import javax.inject.Inject

const val INCLUDE_PREFIX = "// include "
const val DEF_PREFIX = "def "
const val DECL_PREFIX = "decl "

class ParseDefUseCase {
    @Inject
    lateinit var locatorUseCase: LocatorUseCase

    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var processCommentUseCase: ProcessCommentUseCase

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseDef(path: String): List<Insert> {
        val located = locatorUseCase.locate(path)
        val tokenStream = parsingManager.createTokenStream(located.file)
        val parser = parsingManager.createParser(tokenStream)
        val inserts = parsingManager.parseMethodDef(tokenStream, parser, located.clazz, located.member)
        return processInserts(inserts)
    }

    private fun parseDecl(path: String): List<Insert> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun processInserts(inserts: List<Insert>) : List<Insert> {
        val result = ArrayList<Insert>()
        for (insert in inserts) {
            when (insert) {
                is InsertCommand -> {
                    result.addAll(processCommand(insert.command))
                }
                is InsertComment -> {
                    result.add(processCommentUseCase.processComment(insert.comment))
                }
                else -> result.add(insert)
            }
        }
        return result
    }

    private fun processCommand(command: String): List<Insert> {
        check(command.startsWith(INCLUDE_PREFIX)) { "Expected command is not started with expected prefix!" }
        val include = command.removePrefix(INCLUDE_PREFIX)
        return processInclude(include)
    }

    private fun processInclude(include: String): List<Insert> {
        if (include.startsWith(DECL_PREFIX)) {
            val decl = include.removePrefix(DECL_PREFIX)
            return parseDecl(decl)
        }
        if (include.startsWith(DEF_PREFIX)) {
            val def = include.removePrefix(DEF_PREFIX)
            return parseDef(def)
        }
        throw IllegalStateException("Wtf??")
    }
}