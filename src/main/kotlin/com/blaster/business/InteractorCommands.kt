package com.blaster.business

import com.blaster.data.nodes.*
import com.blaster.platform.LEM_COMPONENT
import dagger.Lazy

import java.io.File
import java.net.URL
import javax.inject.Inject

class InteractorCommands {
    @Inject
    lateinit var interactorParse: Lazy<InteractorParse>

    @Inject
    lateinit var interactorLocation: InteractorLocation

    init {
        LEM_COMPONENT.inject(this)
    }

    fun identifyCommands(sourceUrl: URL, sourceRoot: File, nodes: List<Node>): List<Node> = nodes.map {
        when (it) {
            is NodeText -> identifyCommand(sourceUrl, sourceRoot, it.text) ?: it
            else -> it
        }
    }

    // Main commands identification routine. Will return a command if identified, or null if nothing found
    private fun identifyCommand(sourceUrl: URL, sourceRoot: File, command: String): NodeCommand? {
        // We prefix all of the commands in the text with a '#' symbol. If it is not found - it is not a command
        if (!command.startsWith(COMMAND_IDENTIFIER)) {
            return null
        }
        // Removing the prefix and converting a command into a stack of words
        val noPrefix = command.removePrefix(COMMAND_IDENTIFIER)
        val stack = splitCsv(noPrefix)
        val cmd = stack[0]
        val subcmd = stack.subList(1, stack.size)
        // Then identifying each command family by command name. We remove the head of the stack each time when we go to the next level. Each command family will be parsed in the similar fashion, until nothing is left on the stack
        return when {
            cmd == COMMAND_INCLUDE -> identifyIncludeCommand(sourceUrl, sourceRoot, subcmd)
            cmd == COMMAND_HEADER -> identifyHeaderCommand(subcmd)
            cmd == COMMAND_PICTURE -> identifyPictureCommand(subcmd)
            cmd == COMMAND_INLINE -> identifyInlineCommand(sourceUrl, sourceRoot, subcmd)
            cmd == COMMAND_OMIT -> identifyOmitCommand()
            cmd == COMMAND_CITE -> identifyCiteCommand(subcmd)
            else -> TODO()
        }
    }

    private fun identifyIncludeCommand(sourceUrl: URL, sourceRoot: File, stack: List<String>): NodeCommand? {
        val location = interactorLocation.locate(sourceUrl, sourceRoot, stack[1])
        val subcmd = stack[0]
        val cmd = when {
            subcmd == SUBCOMMAND_DECL -> NodeCommand(NodeCommand.Type.INCLUDE, listOf(SUBCOMMAND_DECL, stack[1]), location)
            subcmd == SUBCOMMAND_DEF -> NodeCommand(NodeCommand.Type.INCLUDE, listOf(SUBCOMMAND_DEF, stack[1]), location)
            else -> TODO()
        }
        return cmd
    }

    private fun identifyInlineCommand(sourceUrl: URL, sourceRoot: File, stack: List<String>): NodeCommand? {
        val location = interactorLocation.locate(sourceUrl, sourceRoot, stack[1])
        val subcmd = stack[0]
        val cmd =  when {
            subcmd == SUBCOMMAND_DECL -> NodeCommand(NodeCommand.Type.INLINE, listOf(SUBCOMMAND_DECL, stack[1]), location)
            subcmd == SUBCOMMAND_DEF -> NodeCommand(NodeCommand.Type.INLINE, listOf(SUBCOMMAND_DEF, stack[1]), location)
            else -> TODO()
        }
        return cmd
    }

    private fun identifyHeaderCommand(stack: List<String>): NodeCommand? {
        val subcmd = stack[0]
        val cmd = when {
            subcmd == SUBCOMMAND_H1 -> NodeCommand(NodeCommand.Type.HEADER, listOf(SUBCOMMAND_H1, stack[1]))
            subcmd == SUBCOMMAND_H2 -> NodeCommand(NodeCommand.Type.HEADER, listOf(SUBCOMMAND_H2, stack[1]))
            else -> TODO()
        }
        return cmd
    }

    private fun identifyPictureCommand(subcmd: List<String>): NodeCommand? {
        check(subcmd.size == 2) { "Wrong amount of parameters for a picture command!" }
        return NodeCommand(NodeCommand.Type.PICTURE, listOf(subcmd[0], subcmd[1]))
    }

    private fun identifyOmitCommand(): NodeCommand? {
        return NodeCommand(NodeCommand.Type.OMIT, listOf())
    }

    private fun identifyCiteCommand(stack: List<String>): NodeCommand? {
        check(stack.size == 3) { "Cite command has to have 3 parameters! ${stack.joinToString { "" }}" }
        return NodeCommand(NodeCommand.Type.CITE, listOf(stack[0], stack[1], stack[2]))
    }

    // Commands application routine. It receives a source url and root and a list of nodes as a parameters. The result is a list of nodes modified by all of the commands in the original list.
    fun applyCommands(sourceUrl: URL, sourceRoot: File, nodes: List<Node>): List<Node> {
        // Since we will do operations on the list, we want to convert it to a mutable one - the structure of the list can be changed this way
        val mutableList = ArrayList(nodes)
        val iterator = mutableList.listIterator()
        // We iterate over the list until we reached the end
        while (iterator.hasNext()) {
            val node = iterator.next()
            // If the next item in the list is a command
            if (node is NodeCommand) {
                when (node.type) {
                    // We apply the command accordingly
                    NodeCommand.Type.INCLUDE -> applyIncludeCommand(sourceUrl, sourceRoot, iterator, node)
                    NodeCommand.Type.OMIT -> applyOmitCommand(iterator)
                    NodeCommand.Type.INLINE -> applyInlineCommand(sourceUrl, sourceRoot, iterator, node)
                    // Some commands have meaning only for printing, so we do nothing right now
                    else -> {}
                }
            }
        }
        // Returning the modified result
        return mutableList
    }

    private fun applyInlineCommand(
        sourceUrl: URL, sourceRoot: File, iterator: MutableListIterator<Node>, insert: NodeCommand) {
        iterator.remove()
        when (insert.subcommand) {
            SUBCOMMAND_DECL -> {
                val declarations = interactorParse.get().parseDecl(sourceUrl, sourceRoot, insert.location!!)
                declarations.forEach { iterator.add(it) }
            }
            SUBCOMMAND_DEF -> {
                val definitions = interactorParse.get().parseDef(sourceUrl, sourceRoot, insert.location!!)
                definitions.forEach { iterator.add(it) }
            }
            else -> TODO()
        }
    }

    private fun applyIncludeCommand(
        sourceUrl: URL, sourceRoot: File, iterator: MutableListIterator<Node>, insert: NodeCommand) {
        iterator.remove()
        val children = when (insert.subcommand) {
            SUBCOMMAND_DECL -> interactorParse.get().parseDecl(sourceUrl, sourceRoot, insert.location!!)
            SUBCOMMAND_DEF -> interactorParse.get().parseDef(sourceUrl, sourceRoot, insert.location!!)
            else -> TODO()
        }
        iterator.add(insert.copy(children = children))
    }

    private fun applyOmitCommand(iterator: MutableListIterator<Node>) {
        check(iterator.hasNext()) { "What to omit??" }
        iterator.remove()
        iterator.next()
        iterator.remove()
    }
}