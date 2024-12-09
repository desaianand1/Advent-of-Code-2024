package `2024`.day09

import utilities.readInput

data class FileBlock(val id: Int, val size: Int)
data class FileSpan(val fileBlock: FileBlock, val startIndex: Int, val endIndex: Int)

fun main() {
    val input = readInput("2024/day09/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Long {
    val diskMap = input.first()
    return calculateChecksum(compactDisk(expandFileBlocks(parseDiskMap(diskMap))))
}

fun part2(input: List<String>): Long {
    val diskMap = input.first()
    return calculateChecksum(compactDiskWholeFiles(expandFileBlocks(parseDiskMap(diskMap))))
}

fun parseDiskMap(diskMap: String): List<Pair<FileBlock?, Int>> {
    val sections = diskMap.map { it.toString().toInt() }
    val blocks = mutableListOf<Pair<FileBlock?, Int>>()
    var fileId = 0

    for (i in sections.indices step 2) {
        blocks.add(Pair(FileBlock(fileId, sections[i]), sections[i]))
        fileId++
        if (i + 1 < sections.size) {
            blocks.add(Pair(null, sections[i + 1]))
        }
    }

    return blocks
}

fun expandFileBlocks(blocks: List<Pair<FileBlock?, Int>>): MutableList<FileBlock?> {
    val diskPositions = mutableListOf<FileBlock?>()

    blocks.forEach { (fileBlock, size) ->
        // expand blocks into individual positions
        repeat(size) {
            diskPositions.add(fileBlock)
        }
    }
    return diskPositions
}

fun compactDisk(diskPositions: MutableList<FileBlock?>): List<FileBlock?> {
    for (pass in 0 until diskPositions.size) {
        var changed = false

        // right to left
        for (i in diskPositions.lastIndex downTo 1) {
            val currentFile = diskPositions[i]
            if (currentFile != null) {
                // find leftmost free space
                for (j in 0 until i) {
                    if (diskPositions[j] == null) {
                        // move file block to free space
                        diskPositions[j] = currentFile
                        diskPositions[i] = null
                        changed = true
                        break
                    }
                }
            }
        }
        if (!changed) {
            break
        }
    }

    return diskPositions
}

fun compactDiskWholeFiles(diskPositions: MutableList<FileBlock?>): List<FileBlock?> {
    val fileSpans = findFileSpans(diskPositions)

    // in order of decreasing file ID
    fileSpans.sortedByDescending { it.fileBlock.id }.forEach { fileSpan ->
        val freeSpaceStart = findLeftmostFreeSpace(diskPositions, 0, fileSpan.startIndex, fileSpan.fileBlock.size)

        if (freeSpaceStart != -1) {
            val fileSize = fileSpan.fileBlock.size
            // clear original position
            for (i in fileSpan.startIndex..fileSpan.endIndex) {
                diskPositions[i] = null
            }
            // place in new position
            for (i in 0 until fileSize) {
                diskPositions[freeSpaceStart + i] = fileSpan.fileBlock
            }
        }
    }

    return diskPositions
}

fun findFileSpans(diskPositions: List<FileBlock?>): List<FileSpan> {
    val spans = mutableListOf<FileSpan>()
    var currentStart = -1
    var currentFile: FileBlock? = null

    for (i in diskPositions.indices) {
        val position = diskPositions[i]
        if (position != null) {
            if (currentFile == null || currentFile.id != position.id) {
                if (currentFile != null) {
                    spans.add(FileSpan(currentFile, currentStart, i - 1))
                }
                currentStart = i
                currentFile = position
            }
        } else if (currentFile != null) {
            spans.add(FileSpan(currentFile, currentStart, i - 1))
            currentFile = null
        }
    }

    // handle last file if it extends to the end
    if (currentFile != null) {
        spans.add(FileSpan(currentFile, currentStart, diskPositions.lastIndex))
    }

    return spans
}

fun findLeftmostFreeSpace(diskPositions: List<FileBlock?>, start: Int, limit: Int, size: Int): Int {
    var consecutiveFree = 0
    var potentialStart = -1

    for (i in start until limit) {
        if (diskPositions[i] == null) {
            if (consecutiveFree == 0) {
                potentialStart = i
            }
            consecutiveFree++
            if (consecutiveFree == size) {
                return potentialStart
            }
        } else {
            consecutiveFree = 0
            potentialStart = -1
        }
    }

    return -1
}

fun calculateChecksum(diskPositions: List<FileBlock?>): Long {
    return diskPositions.mapIndexed { index, fileBlock ->
        if (fileBlock != null) {
            index.toLong() * fileBlock.id.toLong()
        } else {
            0L
        }
    }.sum()
}
