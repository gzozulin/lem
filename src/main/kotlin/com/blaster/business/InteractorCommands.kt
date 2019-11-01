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

    private fun identifyCommand(command: String): ParagraphCommand? {
        if (!command.startsWith(COMMAND_IDENTIFIER)) {
            return null
        }
        val noPrefix = command.removePrefix(COMMAND_IDENTIFIER)
        val stack = noPrefix.split(CSV_PATTERN)
        val first = stack[0]
        return when {
            first == COMMAND_INCLUDE -> identifyIncludeCommand(stack.subList(1, stack.size))
            first == COMMAND_HEADER -> identifyHeaderCommand(stack.subList(1, stack.size))
            first == COMMAND_INLINE -> identifyInlineCommand(stack.subList(1, stack.size))
            first == COMMAND_OMIT -> identifyOmitCommand()
            else -> throw IllegalStateException("Unknown command! $first")
        }
    }

    private fun identifyIncludeCommand(stack: List<String>): ParagraphCommand? {
        val first = stack[0]
        return when {
            first == SUBCOMMAND_DECL -> ParagraphCommand(ParagraphCommand.Type.INCLUDE, listOf(SUBCOMMAND_DECL, stack[1]))
            first == SUBCOMMAND_DEF -> ParagraphCommand(ParagraphCommand.Type.INCLUDE, listOf(SUBCOMMAND_DEF, stack[1]))
            first == SUBCOMMAND_LINK -> {
                check(stack.size == 4) { "Wrong amount of parameters for a link include command!" }
                ParagraphCommand(ParagraphCommand.Type.INCLUDE, listOf(SUBCOMMAND_LINK, stack[1], stack[2], stack[3]))
            }
            first == SUBCOMMAND_PICTURE -> {
                check(stack.size == 4) { "Wrong amount of parameters for a link include command!" }
                ParagraphCommand(
                    ParagraphCommand.Type.INCLUDE,
                    listOf(SUBCOMMAND_PICTURE, stack[1], stack[2], stack[3])
                )
            }
            else -> throw IllegalStateException("Unknown subcommand! $first")
        }
    }

    private fun identifyHeaderCommand(stack: List<String>): ParagraphCommand? {
        val first = stack[0]
        return when {
            first == SUBCOMMAND_H1 -> ParagraphCommand(ParagraphCommand.Type.HEADER, listOf(SUBCOMMAND_H1, stack[1]))
            first == SUBCOMMAND_H2 -> ParagraphCommand(ParagraphCommand.Type.HEADER, listOf(SUBCOMMAND_H2, stack[1]))
            else -> throw IllegalStateException("Unknown subcommand! $first")
        }
    }

    private fun identifyInlineCommand(stack: List<String>): ParagraphCommand? {
        val first = stack[0]
        return when {
            first == SUBCOMMAND_DECL -> ParagraphCommand(ParagraphCommand.Type.INLINE, listOf(SUBCOMMAND_DECL, stack[1]))
            first == SUBCOMMAND_DEF -> ParagraphCommand(ParagraphCommand.Type.INLINE, listOf(SUBCOMMAND_DEF, stack[1]))
            else -> throw IllegalStateException("Unknown subcommand! $first")
        }
    }

    private fun identifyOmitCommand(): ParagraphCommand? {
        return ParagraphCommand(ParagraphCommand.Type.OMIT, listOf())
    }

    fun applyCommands(sourceRoot: File, paragraphs: List<Paragraph>): List<Paragraph> {
        val mutableList = ArrayList(paragraphs)
        val iterator = mutableList.listIterator()
        while (iterator.hasNext()) {
            val insert = iterator.next()
            if (insert is ParagraphCommand) {
                when (insert.type) {
                    ParagraphCommand.Type.INCLUDE -> applyIncludeCommand(insert, sourceRoot)
                    ParagraphCommand.Type.OMIT -> applyOmitCommand(iterator)
                    ParagraphCommand.Type.INLINE -> applyInlineCommand(iterator, insert, sourceRoot)

                    // else do nothing
                    else -> { }
                }
            }
        }
        return mutableList
    }

    private fun applyInlineCommand(
        iterator: MutableListIterator<Paragraph>,
        insert: ParagraphCommand,
        sourceRoot: File
    ) {
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