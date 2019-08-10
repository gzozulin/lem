package com.blaster.data.inserts

data class InsertCommand(
    val path: String,
    val type: Type,
    val inclType: IncludeType = IncludeType.DECL
) : Insert {

    enum class Type { INCLUDE, OMIT }
    enum class IncludeType { DECL, DEF }

    val children = ArrayList<Insert>()
}