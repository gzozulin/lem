package com.blaster.business

import com.blaster.data.inserts.*
import com.blaster.platform.LEM_COMPONENT
import dagger.Lazy
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject

val CSV_PATTERN = ";\\s?".toPattern()

class InteractorCommands {
    @Inject
    lateinit var interactorParse: Lazy<InteractorParse>

    init {
        LEM_COMPONENT.inject(this)
    }

    fun identifyCommands(inserts: List<Insert>): List<Insert> = Observable.fromIterable(inserts)
        .map {
            if (it is InsertText) {
                identifyCommand(it.text) ?: it
            } else {
                it
            }
        }
        .toList()
        .blockingGet()

    private fun identifyCommand(command: String): InsertCommand? {
        if (!command.startsWith(COMMAND_IDENTIFIER)) {
            return null
        }
        val noPrefix = command.removePrefix(COMMAND_IDENTIFIER)
        val stack = noPrefix.split(CSV_PATTERN)
        return when (val first = stack[0]) {
            COMMAND_INCLUDE -> identifyIncludeCommand(stack.subList(1, stack.size))
            COMMAND_HEADER -> identifyHeaderCommand(stack.subList(1, stack.size))
            COMMAND_INLINE -> identifyInlineCommand(stack.subList(1, stack.size))
            COMMAND_OMIT -> identifyOmitCommand()
            else -> throw IllegalStateException("Unknown command! $first")
        }
    }

    private fun identifyIncludeCommand(stack: List<String>): InsertCommand? {
        return when(val first = stack[0]) {
            SUBCOMMAND_DECL -> InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_DECL, stack[1]))
            SUBCOMMAND_DEF -> InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_DEF, stack[1]))
            SUBCOMMAND_LINK -> {
                check(stack.size == 4) { "Wrong amount of parameters for a link include command!" }
                InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_LINK, stack[1], stack[2], stack[3]))
            }
            SUBCOMMAND_PICTURE -> {
                check(stack.size == 4) { "Wrong amount of parameters for a link include command!" }
                InsertCommand(InsertCommand.Type.INCLUDE, listOf(SUBCOMMAND_PICTURE, stack[1], stack[2], stack[3]))
            }
            else -> throw IllegalStateException("Unknown subcommand! $first")
        }
    }

    private fun identifyHeaderCommand(stack: List<String>): InsertCommand? {
        return when (val first = stack[0]) {
            SUBCOMMAND_H1 -> InsertCommand(InsertCommand.Type.HEADER, listOf(SUBCOMMAND_H1, stack[1]))
            SUBCOMMAND_H2 -> InsertCommand(InsertCommand.Type.HEADER, listOf(SUBCOMMAND_H2, stack[1]))
            else -> throw IllegalStateException("Unknown subcommand! $first")
        }
    }

    private fun identifyInlineCommand(stack: List<String>): InsertCommand? {
        return when (val first = stack[0]) {
            SUBCOMMAND_DECL -> InsertCommand(InsertCommand.Type.INLINE, listOf(SUBCOMMAND_DECL, stack[1]))
            SUBCOMMAND_DEF -> InsertCommand(InsertCommand.Type.INLINE, listOf(SUBCOMMAND_DEF, stack[1]))
            else -> throw IllegalStateException("Unknown subcommand! $first")
        }
    }

    private fun identifyOmitCommand(): InsertCommand? {
        return InsertCommand(InsertCommand.Type.OMIT, listOf())
    }

    fun applyCommands(sourceRoot: File, inserts: List<Insert>): List<Insert> {
        val mutableList = ArrayList(inserts)
        val iterator = mutableList.listIterator()
        while (iterator.hasNext()) {
            val insert = iterator.next()
            if (insert is InsertCommand) {
                when (insert.type) {
                    InsertCommand.Type.INCLUDE -> applyIncludeCommand(insert, sourceRoot)
                    InsertCommand.Type.OMIT -> applyOmitCommand(iterator)
                    InsertCommand.Type.INLINE -> applyInlineCommand(iterator, insert, sourceRoot)
                    else -> {} // do nothing
                }
            }
        }
        return mutableList
    }

    private fun applyInlineCommand(iterator: MutableListIterator<Insert>, insert: InsertCommand, sourceRoot: File) {
        iterator.remove()
        when (insert.subcommand) {
            SUBCOMMAND_DECL -> {
                val declarations = interactorParse.get().parseDecl(sourceRoot, insert.argument)
                declarations.forEach { iterator.add(it) }
            }
            SUBCOMMAND_DEF -> {
                val definitions = interactorParse.get().parseDef(sourceRoot, insert.argument)
                definitions.forEach { iterator.add(it) }
            }
        }
    }

    private fun applyIncludeCommand(insert: InsertCommand, sourceRoot: File) {
        when (insert.subcommand) {
            SUBCOMMAND_DECL -> insert.children.addAll(interactorParse.get().parseDecl(sourceRoot, insert.argument))
            SUBCOMMAND_DEF -> insert.children.addAll(interactorParse.get().parseDef(sourceRoot, insert.argument))
        }
    }

    private fun applyOmitCommand(iterator: MutableListIterator<Insert>) {
        check(iterator.hasNext()) { "What to omit??" }
        iterator.remove()
        iterator.next()
        iterator.remove()
    }
}