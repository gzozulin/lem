package com.blaster.business

import com.blaster.data.paragraphs.*
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

    fun identifyCommands(paragraphs: List<Paragraph>): List<Paragraph> = Observable.fromIterable(paragraphs)
        .map {
            if (it is ParagraphText) {
                identifyCommand(it.text) ?: it
            } else {
                it
            }
        }
        .toList()
        .blockingGet()

    // Main commands identification routine. Will return a command if identified, or null if nothing found
    private fun identifyCommand(command: String): ParagraphCommand? {
        // We prefix all of the commands in the text with a '#' symbol. If it is not found - it is not a command
        if (!command.startsWith(COMMAND_IDENTIFIER)) {
            return null
        }
        // Removing the prefix and converting a command into a stack of words
        val noPrefix = command.removePrefix(COMMAND_IDENTIFIER)
        val stack = noPrefix.split(CSV_PATTERN)
        val cmd = stack[0]
        val subcmd = stack.subList(1, stack.size)
        // Then identifying each command family by command name. We remove the head of the stack each time when we go to the next level. Each command family will be parsed in the similar fashion, until nothing is left on the stack
        return when {
            cmd == COMMAND_INCLUDE -> identifyIncludeCommand(subcmd)
            cmd == COMMAND_HEADER -> identifyHeaderCommand(subcmd)
            cmd == COMMAND_INLINE -> identifyInlineCommand(subcmd)
            cmd == COMMAND_OMIT -> identifyOmitCommand()
            else -> throw IllegalStateException("Unknown command! $cmd")
        }
    }

    private fun identifyIncludeCommand(stack: List<String>): ParagraphCommand? {
        val subcmd = stack[0]
        return when {
            subcmd == SUBCOMMAND_DECL -> ParagraphCommand(ParagraphCommand.Type.INCLUDE, listOf(SUBCOMMAND_DECL, stack[1]))
            subcmd == SUBCOMMAND_DEF -> ParagraphCommand(ParagraphCommand.Type.INCLUDE, listOf(SUBCOMMAND_DEF, stack[1]))
            subcmd == SUBCOMMAND_LINK -> {
                check(stack.size == 4) { "Wrong amount of parameters for a link include command!" }
                ParagraphCommand(ParagraphCommand.Type.INCLUDE, listOf(SUBCOMMAND_LINK, stack[1], stack[2], stack[3]))
            }
            subcmd == SUBCOMMAND_PICTURE -> {
                check(stack.size == 4) { "Wrong amount of parameters for a link include command!" }
                ParagraphCommand(
                    ParagraphCommand.Type.INCLUDE,
                    listOf(SUBCOMMAND_PICTURE, stack[1], stack[2], stack[3])
                )
            }
            else -> throw IllegalStateException("Unknown subcommand! $subcmd")
        }
    }

    private fun identifyHeaderCommand(stack: List<String>): ParagraphCommand? {
        val subcmd = stack[0]
        return when {
            subcmd == SUBCOMMAND_H1 -> ParagraphCommand(ParagraphCommand.Type.HEADER, listOf(SUBCOMMAND_H1, stack[1]))
            subcmd == SUBCOMMAND_H2 -> ParagraphCommand(ParagraphCommand.Type.HEADER, listOf(SUBCOMMAND_H2, stack[1]))
            else -> throw IllegalStateException("Unknown subcommand! $subcmd")
        }
    }

    private fun identifyInlineCommand(stack: List<String>): ParagraphCommand? {
        val subcmd = stack[0]
        return when {
            subcmd == SUBCOMMAND_DECL -> ParagraphCommand(ParagraphCommand.Type.INLINE, listOf(SUBCOMMAND_DECL, stack[1]))
            subcmd == SUBCOMMAND_DEF -> ParagraphCommand(ParagraphCommand.Type.INLINE, listOf(SUBCOMMAND_DEF, stack[1]))
            else -> throw IllegalStateException("Unknown subcommand! $subcmd")
        }
    }

    private fun identifyOmitCommand(): ParagraphCommand? {
        return ParagraphCommand(ParagraphCommand.Type.OMIT, listOf())
    }

    // Commands application routine. It receives a source root and a list of paragraphs as a parameters. The result is a list of paragraphs modified by all of the commands in the original list.
    fun applyCommands(sourceRoot: File, paragraphs: List<Paragraph>): List<Paragraph> {
        // Since we will do operations on the list, we want to convert it to a mutable one - the structure of the list can be changed this way
        val mutableList = ArrayList(paragraphs)
        val iterator = mutableList.listIterator()
        // We iterate over the list until we reached the end
        while (iterator.hasNext()) {
            val paragraph = iterator.next()
            // If the next item in the list is a command
            if (paragraph is ParagraphCommand) {
                when (paragraph.type) {
                    // We apply the command accordingly
                    ParagraphCommand.Type.INCLUDE -> applyIncludeCommand(paragraph, sourceRoot)
                    ParagraphCommand.Type.OMIT -> applyOmitCommand(iterator)
                    ParagraphCommand.Type.INLINE -> applyInlineCommand(iterator, paragraph, sourceRoot)
                    // Some commands have meaning only for printing, so we do nothing right now
                    else -> { }
                }
            }
        }
        // Returning the modified result
        return mutableList
    }

    private fun applyInlineCommand(iterator: MutableListIterator<Paragraph>, insert: ParagraphCommand, sourceRoot: File) {
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

    private fun applyIncludeCommand(insert: ParagraphCommand, sourceRoot: File) {
        when (insert.subcommand) {
            SUBCOMMAND_DECL -> insert.children.addAll(interactorParse.get().parseDecl(sourceRoot, insert.argument))
            SUBCOMMAND_DEF -> insert.children.addAll(interactorParse.get().parseDef(sourceRoot, insert.argument))
        }
    }

    private fun applyOmitCommand(iterator: MutableListIterator<Paragraph>) {
        check(iterator.hasNext()) { "What to omit??" }
        iterator.remove()
        iterator.next()
        iterator.remove()
    }
}