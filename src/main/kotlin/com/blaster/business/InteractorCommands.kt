package com.blaster.business

import com.blaster.data.inserts.InsertCommand
import java.lang.IllegalArgumentException

const val COMMAND_INCLUDE       = "include"
const val COMMAND_HEADER        = "header"
const val COMMAND_OMIT          = "omit"

const val SUBCOMMAND_DECL       = "decl"
const val SUBCOMMAND_DEF        = "def"
const val SUBCOMMAND_H1         = "h1"
const val SUBCOMMAND_H2         = "h2"

class InteractorCommands {
    fun extractCommand(command: String): InsertCommand? {
        when {
            command.startsWith(COMMAND_INCLUDE) -> {
                val includeCmd = removeCommandPrefix(command, COMMAND_INCLUDE)
                return when {
                    includeCmd.startsWith(SUBCOMMAND_DECL) -> {
                        val path = removeCommandPrefix(includeCmd, SUBCOMMAND_DECL)
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_DECL, path))
                    }
                    includeCmd.startsWith(SUBCOMMAND_DEF) -> {
                        val path = removeCommandPrefix(includeCmd, SUBCOMMAND_DEF)
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_DEF, path))
                    }
                    else -> throw IllegalArgumentException("Unknown command! $command")
                }
            }
            command.startsWith(COMMAND_HEADER) -> {
                val subcommand = removeCommandPrefix(command, COMMAND_HEADER)
                return when {
                    subcommand.startsWith(SUBCOMMAND_H1) -> {
                        val header = removeCommandPrefix(subcommand, SUBCOMMAND_H1)
                        InsertCommand(InsertCommand.Type.HEADER, listOf(SUBCOMMAND_H1, header))
                    }
                    subcommand.startsWith(SUBCOMMAND_H2) -> {
                        val header = removeCommandPrefix(subcommand, SUBCOMMAND_H2)
                        InsertCommand(InsertCommand.Type.HEADER, listOf(SUBCOMMAND_H2, header))
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

    private fun removeCommandPrefix(text: String, command: String): String {
        val noCommand = text.removePrefix(command)
        return if (noCommand.startsWith(" ")) {
            noCommand.removePrefix(" ")
        } else {
            noCommand
        }
    }
}