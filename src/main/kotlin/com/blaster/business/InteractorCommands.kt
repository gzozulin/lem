package com.blaster.business

import com.blaster.data.inserts.*
import java.lang.IllegalArgumentException

val CSV_PATTERN = ";".toPattern()

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
                    includeCmd.startsWith(SUBCOMMAND_LINK) -> {
                        val csv = removeCommandPrefix(includeCmd, SUBCOMMAND_LINK)
                        val split = csv.split(CSV_PATTERN)
                        check(split.size == 3) { "Wrong amount of parameters for a link include command!" }
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_LINK, split[0], split[1], split[2]))
                    }
                    includeCmd.startsWith(SUBCOMMAND_PICTURE) -> {
                        val csv = removeCommandPrefix(includeCmd, SUBCOMMAND_PICTURE)
                        val split = csv.split(CSV_PATTERN)
                        check(split.size == 3) { "Wrong amount of parameters for a picture include command!" }
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_PICTURE, split[0], split[1], split[2]))
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
            command.startsWith(COMMAND_INLINE) -> {
                val inlineCmd = removeCommandPrefix(command, COMMAND_INLINE)
                return when {
                    inlineCmd.startsWith(SUBCOMMAND_DECL) -> {
                        val path = removeCommandPrefix(inlineCmd, SUBCOMMAND_DECL)
                        InsertCommand(InsertCommand.Type.INLINE, listOf(SUBCOMMAND_DECL, path))
                    }
                    inlineCmd.startsWith(SUBCOMMAND_DEF) -> {
                        val path = removeCommandPrefix(inlineCmd, SUBCOMMAND_DEF)
                        InsertCommand(InsertCommand.Type.INLINE, listOf(SUBCOMMAND_DEF, path))
                    }
                    else -> throw IllegalArgumentException("Unknown command! $command")
                }
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