package `2024`.day11

import utilities.readInput

fun main() {
    val input = readInput("2024/day11/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun Long.countDigits(): Int = when (this) {
    0L -> 1
    else -> this.toString().length
}

private val cache = mutableMapOf<String, Long>()
private fun createCacheKey(stone: Long, blink: Int) = "$stone,$blink"

private fun simulateBlinks(stones: List<Long>, numBlinks: Int): Long {
    cache.clear()
    return stones.sumOf { stone -> countStoneSplits(stone, numBlinks) }
}

private fun countStoneSplits(stone: Long, blink: Int): Long {
    val cacheKey = createCacheKey(stone, blink)
    if (cache.containsKey(cacheKey)) {
        return cache[cacheKey]!!
    }
    if (blink == 0) {
        return 1L
    }
    val stoneCount = when {
        // Rule 1: If stone is 0, replace with 1
        stone == 0L -> countStoneSplits(1, blink - 1)
        // Rule 2: If stone has even number of digits, it is replaced by two stones
        stone.countDigits() % 2 == 0 -> {
            val stoneStr = stone.toString()
            val numDigits = stoneStr.length
            val mid = numDigits / 2
            return countStoneSplits(
                stoneStr.substring(0, mid).toLong(),
                blink - 1,
            ) + countStoneSplits(stoneStr.substring(mid).toLong(), blink - 1)
        }
        // Rule 3: If none of the other rules apply, multiply by 2024
        else -> countStoneSplits(stone * 2024, blink - 1)
    }
    cache[cacheKey] = stoneCount
    return stoneCount
}

fun part1(input: List<String>): Long {
    val stones = input[0].split(' ').map { it.toLong() }
    return simulateBlinks(stones, 25)
}

fun part2(input: List<String>): Long {
    val stones = input[0].split(' ').map { it.toLong() }
    return simulateBlinks(stones, 75)
}
