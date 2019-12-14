package com.blaster.business

import java.io.File
import java.net.URL

open class Location(val url: URL, val file: File)

class LocationClass(url: URL, file: File, val clazz: String) : Location(url, file) {
    override fun toString(): String = "{file: $file, class: $clazz}"
}

class LocationMember(url: URL, file: File, val clazz: String, val identifier: String) : Location(url, file) {
    override fun toString(): String = "{file: $file, class: $clazz, identifier: $identifier}"
}

class LocationGlobal(url: URL, file: File, val identifier: String) : Location(url, file) {
    override fun toString(): String = "{file: $file, identifier: $identifier}"
}

val PATH_REGEX = "\\w+(\\.\\w+)+(::\\w+)?".toRegex()

// global method:       com.blaster.platform.LemAppKt::main
// member in class:     com.blaster.platform.LemApp::render
// class:               com.blaster.platform.LemApp

class InteractorLocation {

    // This routine helps us to locate pieces of code, pointed out by path parameter. It returns a class, which represents the location of the found snippet.
    fun locate(sourceUrl: URL, sourceRoot: File, path: String): Location {
        // First of all we want to assert if the path is formatted properly. This allows to highlight errors early
        check(PATH_REGEX.find(path)!!.value.length == path.length) { "Wrong path for the location: $path" }
        // We start by extracting the class from path string. It simply grabs everything before ':'
        val clazz = extractClass(path)
        // Next we want to retreive the actual file, containing the class. We do that by looking at the sources root and a package
        val file = locateFile(sourceRoot, clazz)
        // We also want to assemble the URL to the location
        val url = URL(sourceUrl.toString() + file.toString().replace("\\", "/"))
        // Now we can choose it this is a path to a whole class or to one of its members. We can be sure that the it is a global function or property if the class name ends with 'Kt' according to a Kotlin notation. Else it is a path to a standalone class
        return if (path.contains("::")) {
            val member = extractMember(path)
            if (clazz.endsWith("Kt")) {
                LocationGlobal(url, file, member)
            } else {
                LocationMember(url, file, clazz, member)
            }
        } else {
            LocationClass(url, file, clazz)
        }
    }

    private fun extractClass(path: String): String {
        val lastIndex = path.lastIndexOf(":")
        return path.substring(0, if (lastIndex >= 0) lastIndex - 1 else path.length)
    }

    private fun extractMember(path: String): String {
        return path.substring(path.lastIndexOf(":") + 1, path.length)
    }

    private fun locateFile(sourceRoot: File, clazz: String): File {
        var filepath = clazz.replace(".", "/")
        if (filepath.endsWith("Kt")) {
            filepath = filepath.removeSuffix("Kt")
        }
        filepath += ".kt"
        val result = File(sourceRoot, filepath)
        check(result.exists()) { "Provided class does not exists! $clazz" }
        return result
    }
}