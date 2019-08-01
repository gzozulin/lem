package com.blaster.business

import com.blaster.data.entities.Insert
import com.blaster.data.entities.InsertCode
import com.blaster.platform.LEM_COMPONENT
import javax.inject.Inject

const val INCLUDE_PREFIX = "// include "
const val DECL_PREFIX = "decl "
const val MEMBER_PREFIX = "::"

class ParseCommandUseCase {
    @Inject
    lateinit var parseClassUseCase: ParseClassUseCase

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseCommand(command: String): List<Insert> {
        check(command.startsWith(INCLUDE_PREFIX)) { "Expected command is not started with expected prefix!" }
        val include = command.removePrefix(INCLUDE_PREFIX)
        return parseInclude(include)
    }

    private fun parseInclude(include: String): List<Insert> {
        val decl = include.removePrefix(DECL_PREFIX)
        return parseDecl(decl)
    }

    private fun parseDecl(decl: String): List<Insert> =
        if (decl.contains(MEMBER_PREFIX)) {
            parseMember(decl)
        } else {
            parseClass(decl)
        }

    private fun parseMember(decl: String): List<Insert> {
        // if prop - prop use case
        // if method - loop back to ParseMethodUseCase
        return listOf(InsertCode("Member will go here"))
    }

    private fun parseClass(decl: String): List<Insert> {
        return parseClassUseCase.parseClass(decl)
    }
}