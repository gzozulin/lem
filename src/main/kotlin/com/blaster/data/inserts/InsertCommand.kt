package com.blaster.data.inserts

data class InsertCommand(val type: Type, val arguments: List<String>) : Insert() {
    enum class Type { INCLUDE, OMIT }

    val subcommand: String
        get() = arguments[0]

    val argument: String
        get() = arguments[1]
}