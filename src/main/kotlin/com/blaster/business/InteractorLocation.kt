package com.blaster.business

import java.io.File
import java.net.URL

private val regexPath = """(\w+/)?\w+(\.\w+)+(::\w+)?""".toRegex()

private const val kotlinSources = "src/main/kotlin"

data class Location(val url: URL, val file: File, val clazz: String, val identifier: String)

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
        // If the path contains exact member - extract it. If not - it is the same class
        val identifier = if (modulePath.contains("::")) extractIdentifier(path) else clazz
        return Location(url, file, clazz, identifier)
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

    private fun extractIdentifier(path: String): String {
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