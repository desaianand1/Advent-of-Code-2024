package utilities

import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(path: String) = File(path).readLines()

