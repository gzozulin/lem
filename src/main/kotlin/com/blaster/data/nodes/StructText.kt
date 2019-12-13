package com.blaster.data.nodes

data class StructText(val text: String, val children: List<Node> = listOf()) : Node