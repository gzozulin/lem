package com.blaster.data.paragraphs

const val COMMAND_IDENTIFIER    = "#"

const val COMMAND_INCLUDE       = "include"
const val COMMAND_HEADER        = "header"
const val COMMAND_OMIT          = "omit"
const val COMMAND_INLINE        = "inline"

const val SUBCOMMAND_DECL       = "decl"
const val SUBCOMMAND_DEF        = "def"
const val SUBCOMMAND_LINK       = "link"
const val SUBCOMMAND_PICTURE    = "picture"
const val SUBCOMMAND_H1         = "h1"
const val SUBCOMMAND_H2         = "h2"

data class NodeCommand(val type: Type, val arguments: List<String>) : Node() {
    enum class Type { INCLUDE, OMIT, HEADER, INLINE }

    val subcommand: String
        get() = arguments[0]

    val argument: String
        get() = arguments[1]

    val argument1: String
        get() = arguments[2]

    val argument2: String
        get() = arguments[3]
}