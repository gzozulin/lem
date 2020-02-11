package com.blaster.business

import java.io.File
import java.net.URL

private val regexPath = """(\w+/)?\w+(\.\w+)+(::\w+)?""".toRegex()

private const val kotlinSources = "src/main/kotlin"

sealed class Location {
    abstract val url: URL
    abstract val file: File
}

data class LocationClass(override val url: URL, override val file: File, val clazz: String) : Location() {
    override fun toString(): String = "{file: $file, class: $clazz}"
}

data class LocationMember(override val url: URL, override val file: File, val clazz: String, val identifier: String) : Location() {
    override fun toString(): String = "{file: $file, class: $clazz, identifier: $identifier}"
}

data class LocationGlobal(override val url: URL, override val file: File, val identifier: String) : Location() {
    override fun toString(): String = "{file: $file, identifier: $identifier}"
}

// global method:       com.blaster.platform.LemAppKt::main
// member in class:     com.blaster.platform.LemApp::render
// class:               com.blaster.platform.LemApp
// class in module:     common_gl/com.blaster.gl.glState

class InteractorLocation {
    // This routine helps us to locate pieces of code, pointed out by path parameter. It returns a class, which represents the location of the found snippet.
    fun locate(root: File, sourceUrl: URL, path: String): Location {
        // First of all we want to assert if the path is formatted properly. This allows to highlight errors early
        check(regexPath.find(path)!!.value.length == path.length) { "Wrong path for the location: $path" }
        // We start by extracting module from path if we have one
        val (module, modulePath) = extractModule(path)
        // Then the class follows - we simply grabs everything before ':'
        val clazz = extractClass(modulePath)
        // Now we want to extract filepath to help us with file an url
        val filepath = extractFilepath(clazz)
        // Next we want to retrieve the actual file, containing the class. We do that by looking at the sources root and a package
        val file = locateFile(module, root, filepath)
        // We also want to assemble the URL to the location based source url on Github
        val url = constructUrl(sourceUrl, module, filepath)
        // Now we can choose if this is a path to a whole class or to one of its members. We can be sure that the it is a global function or property if the class name ends with Kt according to a Kotlin notation. Else it is a path to a standalone class
        return if (modulePath.contains("::")) {
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

    private fun extractModule(path: String): Pair<String?, String> {
        return if (path.contains("/")) {
            val index = path.indexOf("/")
            path.substring(0, index) to path.substring(index + 1, path.length)
        } else {
            null to path
        }
    }

    private fun extractClass(path: String): String {
        val lastIndex = path.lastIndexOf(":")
        return path.substring(0, if (lastIndex >= 0) lastIndex - 1 else path.length)
    }

    private fun extractMember(path: String): String {
        return path.substring(path.lastIndexOf(":") + 1, path.length)
    }

    private fun extractFilepath(clazz: String): String {
        var filepath = clazz.replace(".", "/")
        if (filepath.endsWith("Kt")) {
            filepath = filepath.removeSuffix("Kt")
        }
        filepath += ".kt"
        return filepath
    }

    private fun locateFile(module: String?, root: File, filepath: String): File {
        val result = if (module != null) {
            File("${root.path}/$module/$kotlinSources/$filepath")
        } else {
            File("${root.path}/$kotlinSources/$filepath")
        }
        check(result.exists()) { "Provided class does not exists! $filepath" }
        return result
    }

    private fun constructUrl(sourceUrl: URL, module: String?, filepath: String): URL {
        return if (module != null) {
            URL("$sourceUrl/$module/${kotlinSources}/$filepath")
        } else {
            URL("$sourceUrl/${kotlinSources}/$filepath")
        }
    }
}