package com.blaster.business

import com.blaster.platform.LEM_COMPONENT
import java.io.File
import javax.inject.Inject
import javax.inject.Named

open class Location(val file: File)

class LocationClass(file: File, val clazz: String) : Location(file)
class LocationMember(file: File, val clazz: String, val identifier: String) : Location(file)
class LocationGlobal(file: File, val identifier: String) : Location(file)

class InteractorLocation {

    @Inject
    @field:Named("SOURCE_ROOT")
    lateinit var sourceRoot: File

    init {
        LEM_COMPONENT.inject(this)
    }

    // global method:       com.blaster.platform.LemAppKt::main
    // member in class:     com.blaster.platform.LemApp::render
    // class:               com.blaster.platform.LemApp

    fun locate(path: String): Location {
        val clazz = extractClass(path)
        val file = locateFile(clazz)
        return if (path.contains("::")) {
            val member = extractMember(path)
            if (clazz.endsWith("Kt")) {
                LocationGlobal(file, member)
            } else {
                LocationMember(file, clazz, member)
            }
        } else {
            LocationClass(file, clazz)
        }
    }

    private fun extractClass(path: String): String {
        val lastIndex = path.lastIndexOf(":")
        return path.substring(0, if (lastIndex >= 0) lastIndex - 1 else path.length)
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