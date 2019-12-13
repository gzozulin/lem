package com.blaster.data.nodes

data class NodeText(val text: String, val children: List<Node> = listOf()) : Node