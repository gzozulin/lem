package com.blaster.business

import com.blaster.data.inserts.InsertCommand
import java.lang.IllegalArgumentException

const val COMMAND_INCLUDE = "include "
const val COMMAND_OMIT   = "omit"

const val SUBCOMMAND_DECL   = "decl "
const val SUBCOMMAND_DEF    = "def "

class InteractorCommands {
    fun extractCommand(command: String): InsertCommand? {
        when {
            command.startsWith(COMMAND_INCLUDE) -> {
                val includeCmd = command.removePrefix(COMMAND_INCLUDE)
                return when {
                    includeCmd.startsWith(SUBCOMMAND_DECL) -> {
                        val path = includeCmd.removePrefix(SUBCOMMAND_DECL)
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_DECL, path))
                    }
                    includeCmd.startsWith(SUBCOMMAND_DEF) -> {
                        val path = includeCmd.removePrefix(SUBCOMMAND_DEF)
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_DEF, path))
                    }
                    else -> throw IllegalArgumentException("Unknown command! $command")
                }
            }
            command.startsWith(COMMAND_OMIT) -> {
                return InsertCommand(InsertCommand.Type.OMIT, listOf())
            }
            else -> {
                return null
            }
        }
    }
}