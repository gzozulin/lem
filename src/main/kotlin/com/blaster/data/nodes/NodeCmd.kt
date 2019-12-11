package com.blaster.data.nodes

const val COMMAND_IDENTIFIER    = "#"

const val COMMAND_INCLUDE       = "include"
const val COMMAND_HEADER        = "header"
const val COMMAND_PICTURE       = "picture"
const val COMMAND_OMIT          = "omit"
const val COMMAND_INLINE        = "inline"
const val COMMAND_CITE          = "cite"

const val SUBCOMMAND_DECL       = "decl"
const val SUBCOMMAND_DEF        = "def"
const val SUBCOMMAND_H1         = "h1"
const val SUBCOMMAND_H2         = "h2"

data class NodeCommand(val type: Type, val arguments: List<String>) : Node() {
    enum class Type { INCLUDE, OMIT, HEADER, PICTURE, INLINE, CITE }

    val subcommand: String
        get() = arguments[0]

    val argument: String
        get() = arguments[1]

    val argument1: String
        get() = arguments[2]

    val argument2: String
        get() = arguments[3]
}