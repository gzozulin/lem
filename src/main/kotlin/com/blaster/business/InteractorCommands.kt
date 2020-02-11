package com.blaster.business

import com.blaster.data.nodes.*
import com.blaster.platform.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.net.URL

class InteractorCommands {
    private val interactorParse: InteractorParse by kodein.instance()
    private val interactorLocation: InteractorLocation by kodein.instance()

    fun identifyCommands(root: File, sourceUrl: URL, nodes: List<Node>): List<Node> = nodes.map {
        when (it) {
            is NodeText -> identifyCommand(root, sourceUrl, it.text) ?: it
            else -> it
        }
    }

    // Main commands identification routine. Will return a command if identified, or null if nothing found
    private fun identifyCommand(root: File, sourceUrl: URL, command: String): NodeCommand? {
        // We prefix all of the commands in the text with a '#' symbol. If it is not found - it is not a command
        if (!command.startsWith(COMMAND_IDENTIFIER)) {
            return null
        }
        // Removing the prefix and converting a command into a stack of words
        val noPrefix = command.removePrefix(COMMAND_IDENTIFIER)
        val stack = noPrefix.splitCsv()
        val cmd = stack[0]
        val subcmd = stack.subList(1, stack.size)
        // Then identifying each command family by command name. We remove the head of the stack each time when we go to the next level. Each command family will be parsed in the similar fashion, until nothing is left on the stack
        return when {
            cmd == COMMAND_INCLUDE -> identifyIncludeCommand(root, sourceUrl, subcmd)
            cmd == COMMAND_HEADER -> identifyHeaderCommand(subcmd)
            cmd == COMMAND_PICTURE -> identifyPictureCommand(subcmd)
            cmd == COMMAND_INLINE -> identifyInlineCommand(root, sourceUrl, subcmd)
            cmd == COMMAND_OMIT -> identifyOmitCommand()
            cmd == COMMAND_CITE -> identifyCiteCommand(subcmd)
            else -> TODO()
        }
    }

    private fun identifyIncludeCommand(root: File, sourceUrl: URL, stack: List<String>): NodeCommand? {
        val location = interactorLocation.locate(root, sourceUrl, stack[1])
        val subcmd = stack[0]
        val cmd = when {
            subcmd == SUBCOMMAND_DECL -> NodeCommand(CmdType.INCLUDE, listOf(SUBCOMMAND_DECL, stack[1]), location)
            subcmd == SUBCOMMAND_DEF -> NodeCommand(CmdType.INCLUDE, listOf(SUBCOMMAND_DEF, stack[1]), location)
            else -> TODO()
        }
        return cmd
    }

    private fun identifyInlineCommand(root: File, sourceUrl: URL, stack: List<String>): NodeCommand? {
        val location = interactorLocation.locate(root, sourceUrl, stack[1])
        val subcmd = stack[0]
        val cmd =  when {
            subcmd == SUBCOMMAND_DECL -> NodeCommand(CmdType.INLINE, listOf(SUBCOMMAND_DECL, stack[1]), location)
            subcmd == SUBCOMMAND_DEF -> NodeCommand(CmdType.INLINE, listOf(SUBCOMMAND_DEF, stack[1]), location)
            else -> TODO()
        }
        return cmd
    }

    private fun identifyHeaderCommand(stack: List<String>): NodeCommand? {
        val subcmd = stack[0]
        val cmd = when {
            subcmd == SUBCOMMAND_H1 -> NodeCommand(CmdType.HEADER, listOf(SUBCOMMAND_H1, stack[1]))
            subcmd == SUBCOMMAND_H2 -> NodeCommand(CmdType.HEADER, listOf(SUBCOMMAND_H2, stack[1]))
            else -> TODO()
        }
        return cmd
    }

    private fun identifyPictureCommand(subcmd: List<String>): NodeCommand? {
        check(subcmd.size == 2) { "Wrong amount of parameters for a picture command!" }
        return NodeCommand(CmdType.PICTURE, listOf(subcmd[0], subcmd[1]))
    }

    private fun identifyOmitCommand(): NodeCommand? {
        return NodeCommand(CmdType.OMIT, listOf())
    }

    private fun identifyCiteCommand(stack: List<String>): NodeCommand? {
        check(stack.size == 3) { "Cite command has to have 3 parameters! ${stack.joinToString { "" }}" }
        return NodeCommand(CmdType.CITE, listOf(stack[0], stack[1], stack[2]))
    }

    // Commands application routine. It receives a source url and root and a list of nodes as a parameters. The result is a list of nodes modified by all of the commands in the original list.
    fun applyCommands(root: File, sourceUrl: URL, nodes: List<Node>): List<Node> {
        // Since we will do operations on the list, we want to convert it to a mutable one - the structure of the list can be changed this way
        val mutableList = ArrayList(nodes)
        val iterator = mutableList.listIterator()
        // We iterate over the list until we reached the end
        while (iterator.hasNext()) {
            val node = iterator.next()
            // If the next item in the list is a command
            if (node is NodeCommand) {
                when (node.cmdType) {
                    // We apply the command accordingly
                    CmdType.INCLUDE -> applyIncludeCommand(root, sourceUrl, iterator, node)
                    CmdType.OMIT -> applyOmitCommand(iterator)
                    CmdType.INLINE -> applyInlineCommand(root, sourceUrl, iterator, node)
                    // Some commands have meaning only for printing, so we do nothing right now
                    else -> {}
                }
            }
        }
        // Returning the modified result
        return mutableList
    }

    private fun applyInlineCommand(root: File, sourceUrl: URL, iterator: MutableListIterator<Node>, insert: NodeCommand) {
        iterator.remove()
        when (insert.subcommand) {
            SUBCOMMAND_DECL -> {
                val declarations = interactorParse.parseDecl(root, sourceUrl, insert.location!!)
                declarations.forEach { iterator.add(it) }
            }
            SUBCOMMAND_DEF -> {
                val definitions = interactorParse.parseDef(root, sourceUrl, insert.location!!)
                definitions.forEach { iterator.add(it) }
            }
            else -> TODO()
        }
    }

    private fun applyIncludeCommand(root: File, sourceUrl: URL, iterator: MutableListIterator<Node>, insert: NodeCommand) {
        iterator.remove()
        val children = when (insert.subcommand) {
            SUBCOMMAND_DECL -> interactorParse.parseDecl(root, sourceUrl, insert.location!!)
            SUBCOMMAND_DEF -> interactorParse.parseDef(root, sourceUrl, insert.location!!)
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