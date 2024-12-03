package utilities

/**
 * Reads lines from the given input txt file.
 */
public fun readInput(path: String): List<String> =
    object {}.javaClass.classLoader.getResource(path)?.readText()?.trimEnd()?.lines()
        ?: emptyList()
