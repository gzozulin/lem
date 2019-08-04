package com.blaster.business

import com.blaster.platform.LEM_COMPONENT
import java.io.File
import javax.inject.Inject
import javax.inject.Named

open class Location(val file: File)

class ClassLocation(file: File, val clazz: String) : Location(file)
class MemberLocation(file: File, val clazz: String, val identifier: String) : Location(file)
class GlobalLocation(file: File, val identifier: String) : Location(file)

class LocatorInteractor {

    @Inject
    @field:Named("SOURCE_ROOT")
    lateinit var sourceRoot: File

    init {
        LEM_COMPONENT.inject(this)
    }

    // global method:       com.blaster.platform.LemAppKt::main
    // member in class:     com.blaster.platform.LemApp::render
    // class:               com.blaster.platform.LemApp

    fun locate(path: String) : Location {
        val clazz = extractClass(path)
        val file = locateFile(clazz)
        return if (path.contains("::")) {
            val member = extractMember(path)
            if (clazz.endsWith("Kt")) {
                GlobalLocation(file, member)
            } else {
                MemberLocation(file, clazz, member)
            }
        } else {
            ClassLocation(file, clazz)
        }
    }

    private fun extractClass(path: String): String {
        return path.substring(0, path.lastIndexOf(":") - 1)
    }

    private fun extractMember(path: String): String {
        return path.substring(path.lastIndexOf(":") + 1, path.length)
    }

    private fun locateFile(clazz: String): File {
        var filepath = clazz.replace(".", "/")
        if (filepath.endsWith("Kt")) {
            filepath = filepath.removeSuffix("Kt")
        }
        filepath += ".kt"
        val result = File(sourceRoot, filepath)
        check(result.exists()) { "Provided class does not exists!" }
        return result
    }
}