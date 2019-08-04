package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.inserts.InsertComment
import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.platform.LEM_COMPONENT
import java.lang.UnsupportedOperationException
import javax.inject.Inject

const val INCLUDE_PREFIX = "// include "
const val DEF_PREFIX = "def "
const val DECL_PREFIX = "decl "

class ParseInteractor {
    @Inject
    lateinit var locatorInteractor: LocatorInteractor

    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var commentsInteractor: CommentsInteractor

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseDef(path: String): List<Insert> {
        val inserts = when (val location = locatorInteractor.locate(path)) {
            is GlobalLocation -> parsingManager.parseGlobalMethodDef(location)
            is MemberLocation -> parsingManager.parseMemberMethodDef(location)
            else -> throw UnsupportedOperationException()
        }
        return processInserts(inserts)
    }

    private fun parseDecl(path: String): List<Insert> {
        val inserts = when (val location = locatorInteractor.locate(path)) {
            is GlobalLocation -> parsingManager.parseGlobalDecl(location)
            is MemberLocation -> parsingManager.parseMemberDecl(location)
            is ClassLocation -> parsingManager.parseClassDecl(location)
            else -> throw UnsupportedOperationException()
        }
        return processInserts(inserts)
    }

    private fun processInserts(inserts: List<Insert>) : List<Insert> {
        val result = ArrayList<Insert>()
        for (insert in inserts) {
            when (insert) {
                is InsertCommand -> {
                    result.addAll(processCommand(insert.command))
                }
                is InsertComment -> {
                    result.add(commentsInteractor.processComment(insert.comment))
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