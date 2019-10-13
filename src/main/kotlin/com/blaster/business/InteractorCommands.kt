package com.blaster.business

import com.blaster.data.inserts.*
import com.blaster.platform.LEM_COMPONENT
import io.reactivex.Observable
import java.io.File
import java.lang.IllegalArgumentException
import javax.inject.Inject
import dagger.Lazy

val CSV_PATTERN = ";".toPattern()

class InteractorCommands {
    @Inject
    lateinit var interactorParse: Lazy<InteractorParse>

    init {
        LEM_COMPONENT.inject(this)
    }

    fun identifyCommands(inserts: List<Insert>): List<Insert> = Observable.fromIterable(inserts)
        .map {
            if (it is InsertText) {
                extractCommand(it.text) ?: it
            } else {
                it
            }
        }
        .toList()
        .blockingGet()

    fun applyCommands(sourceRoot: File, inserts: List<Insert>): List<Insert> {
        val mutableList = ArrayList(inserts)
        val iterator = mutableList.listIterator()
        while (iterator.hasNext()) {
            val insert = iterator.next()
            if (insert is InsertCommand) {
                when (insert.type) {
                    InsertCommand.Type.INCLUDE -> {
                        when (insert.subcommand) {
                            SUBCOMMAND_DECL -> {
                                insert.children.addAll(interactorParse.get().parseDecl(sourceRoot, insert.argument))
                            }
                            SUBCOMMAND_DEF -> {
                                insert.children.addAll(interactorParse.get().parseDef(sourceRoot, insert.argument))
                            }
                        }
                    }
                    InsertCommand.Type.OMIT -> {
                        check(iterator.hasNext()) { "What to omit??" }
                        iterator.remove()
                        iterator.next()
                        iterator.remove()
                    }
                    InsertCommand.Type.INLINE -> {
                        iterator.remove()
                        when (insert.subcommand) {
                            SUBCOMMAND_DECL -> {
                                val declarations = interactorParse.get().parseDecl(sourceRoot, insert.argument)
                                for (decl in declarations) {
                                    iterator.add(decl)
                                }
                            }
                            SUBCOMMAND_DEF -> {
                                val definitions = interactorParse.get().parseDef(sourceRoot, insert.argument)
                                for (def in definitions) {
                                    iterator.add(def)
                                }
                            }
                        }
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }
        return mutableList
    }

    private fun extractCommand(command: String): InsertCommand? {
        when {
            command.startsWith(COMMAND_INCLUDE) -> {
                val includeCmd = removePrefix(command, COMMAND_INCLUDE)
                return when {
                    includeCmd.startsWith(SUBCOMMAND_DECL) -> {
                        val path = removePrefix(includeCmd, SUBCOMMAND_DECL)
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_DECL, path))
                    }
                    includeCmd.startsWith(SUBCOMMAND_DEF) -> {
                        val path = removePrefix(includeCmd, SUBCOMMAND_DEF)
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_DEF, path))
                    }
                    includeCmd.startsWith(SUBCOMMAND_LINK) -> {
                        val csv = removePrefix(includeCmd, SUBCOMMAND_LINK)
                        val split = csv.split(CSV_PATTERN)
                        check(split.size == 3) { "Wrong amount of parameters for a link include command!" }
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_LINK, split[0], split[1], split[2]))
                    }
                    includeCmd.startsWith(SUBCOMMAND_PICTURE) -> {
                        val csv = removePrefix(includeCmd, SUBCOMMAND_PICTURE)
                        val split = csv.split(CSV_PATTERN)
                        check(split.size == 3) { "Wrong amount of parameters for a picture include command!" }
                        InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_PICTURE, split[0], split[1], split[2]))
                    }
                    else -> throw IllegalArgumentException("Unknown command! $command")
                }
            }
            command.startsWith(COMMAND_HEADER) -> {
                val subcommand = removePrefix(command, COMMAND_HEADER)
                return when {
                    subcommand.startsWith(SUBCOMMAND_H1) -> {
                        val header = removePrefix(subcommand, SUBCOMMAND_H1)
                        InsertCommand(InsertCommand.Type.HEADER, listOf(SUBCOMMAND_H1, header))
                    }
                    subcommand.startsWith(SUBCOMMAND_H2) -> {
                        val header = removePrefix(subcommand, SUBCOMMAND_H2)
                        InsertCommand(InsertCommand.Type.HEADER, listOf(SUBCOMMAND_H2, header))
                    }
                    else -> throw IllegalArgumentException("Unknown command! $command")
                }
            }
            command.startsWith(COMMAND_INLINE) -> {
                val inlineCmd = removePrefix(command, COMMAND_INLINE)
                return when {
                    inlineCmd.startsWith(SUBCOMMAND_DECL) -> {
                        val path = removePrefix(inlineCmd, SUBCOMMAND_DECL)
                        InsertCommand(InsertCommand.Type.INLINE, listOf(SUBCOMMAND_DECL, path))
                    }
                    inlineCmd.startsWith(SUBCOMMAND_DEF) -> {
                        val path = removePrefix(inlineCmd, SUBCOMMAND_DEF)
                        InsertCommand(InsertCommand.Type.INLINE, listOf(SUBCOMMAND_DEF, path))
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

    private fun removePrefix(text: String, command: String): String {
        val noCommand = text.removePrefix(command)
        return if (noCommand.startsWith(" ")) {
            noCommand.removePrefix(" ")
        } else {
            noCommand
        }
    }
}