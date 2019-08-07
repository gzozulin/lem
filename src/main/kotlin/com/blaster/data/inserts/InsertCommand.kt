package com.blaster.data.inserts

class InsertCommand(val command: String) : Insert {
    val children = ArrayList<Insert>()
}