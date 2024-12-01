package utilities

import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(path: String) = File(path).readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = java.security.MessageDigest.getInstance("MD5")
    .digest(toByteArray())
    .joinToString("") { "%02x".format(it) }