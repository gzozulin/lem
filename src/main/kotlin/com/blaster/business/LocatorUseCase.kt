package com.blaster.business

import java.io.File

// src/com/blaster/platform/LemApp:LemApp:main - file : class? : method or property in class

data class LocatedInfo(val file: File, val clazz: String?, val member: String)

class LocatorUseCase {
    fun locate(path: String) : LocatedInfo {
        val split = path.split(":")
        check(split.size == 3) { "Path should contain 3 parts!" }
        val clazz = if (split[1].trim() == "-") { null } else { split[1] }
        return LocatedInfo(File(split[0]), clazz, split[2])
    }
}