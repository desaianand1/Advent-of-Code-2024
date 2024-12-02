package utilities

import java.io.File

/**
 * Reads lines from the given input txt file.
 */
public fun readInput(path: String): List<String> = File(path)
    .takeIf { it.exists() }
    ?.readLines()
    ?: emptyList()

