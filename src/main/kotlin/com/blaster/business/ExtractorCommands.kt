package com.blaster.business

import com.blaster.data.inserts.InsertCommand
import java.lang.IllegalArgumentException

class ExtractorCommands {
    fun extractCommand(command: String): InsertCommand? {
        when {
            command.startsWith("include") -> {
                val includeCmd = command.removePrefix("include ")
                return when {
                    includeCmd.startsWith("decl") -> {
                        val path = includeCmd.removePrefix("decl ")
                        InsertCommand(path, InsertCommand.Type.INCLUDE, InsertCommand.IncludeType.DECL)
                    }
                    includeCmd.startsWith("def") -> {
                        val path = includeCmd.removePrefix("def ")
                        InsertCommand(path, InsertCommand.Type.INCLUDE, InsertCommand.IncludeType.DEF)
                    }
                    else -> throw IllegalArgumentException("Unknown command! $command")
                }
            }
            command.startsWith("omit") -> {
                return InsertCommand("omit", InsertCommand.Type.OMIT)
            }
            else -> {
                return null
            }
        }
    }
}